import { defineStore } from "pinia";
import axios from "axios";
import { debug,info,warning,error } from "./LOG"; 
import { CODE_SHOW, MESSAGE_REQUEST_TYPE, MESSAGE_RESPOSE_TYPE, MESSAGE_SHOW, WELCOME_SHOW } from "./Constant";
import { ElNotification } from "element-plus";
export var url = 'http://localhost:8080/'


export const useTryStore = defineStore('try',{
    state: () => ({
        try1: 'try1',
    })
})

export const useDataStore = defineStore('data',{
    state: () => ({ // 初始值
        // 只读值
        index: 0, // 当前选择数据源的index
        code:'', // 代码数据
        compileWarning:[], // 编译警告
        compileError:[], // 编译错误
        showState:WELCOME_SHOW, // 显示状态 // 0:显示历史数据 1:显示代码
        dataSource:[], // 数据源信息,包含id等内容,建议
        dependencyList:[], // 依赖列表
    }),
    actions: {
        // 增加响应
        addResposeData(respose){
            // respose 是一个字符串
            this.dataSource[this.index].history.push({
                type: MESSAGE_RESPOSE_TYPE,
                time: new Date(),
                data : respose 
            })
            info("增加响应: " + respose)
        },
        setSuggestionData(suggestion){
            this.dataSource[this.index].suggestion = suggestion
        },
        getSuggestionData(){
            if(this.dataSource.length != 0){
                return this.dataSource[this.index].suggestion    
            }else{
                return []
            }
        },
        addRequestData(request){
            this.dataSource[this.index].history.push({
                type: MESSAGE_REQUEST_TYPE,
                time: new Date(),
                data : request
            })
            info("增加询问: " + request)
        },
        // 根据index来切换数据源,更新暂存状态
        async checkoutDependency(to_index){
            this.compileError = []
            this.compileWarning = []
            debug("切换数据源" + "index:" + this.index + "to_index:" + to_index)
            if(to_index < this.dataSource.length){
                if(this.dataSource[to_index].dependency.id == null){
                    info("该数据源未编译,先编译")
                    return;
                }
                await axios.get(url + 'checkout',{
                    params:{
                        id:this.dataSource[to_index].dependency.id,
                        state:this.dataSource[to_index].tempState
                    }
                }).then(res=>{
                    if(res.status === 200){
                        if(res.data.state == 0){
                            info("切换数据源成功")
                            // 更新index的暂存响应值
                            this.index = to_index
                            this.showState = MESSAGE_SHOW
                        }else{
                            error("切换数据源失败")
                        }
                    }else{
                        error("切换数据源失败")
                    }
                })
            }else{
                // 出错了，恢复到欢迎界面
                this.showState = WELCOME_SHOW
                error("切换历史数据源时出错,不存在该数据源")
            }    
        },
        // 获取历史数据
        getHistoryData(){
            if(this.dataSource.length === 0){
                error("数据源为空")
            }else{
                if(this.index >= this.dataSource.length){
                    error("数据源越界")
                }else{
                    return this.dataSource[this.index].history
                }
            }
        },
        // 更新依赖列表
        async pullDependencyList(){
            await axios.get(url + 'dependency').then(res=>{
                if(res.status === 200){
                    // 更新
                    this.dependencyList = res.data.data
                    let dependencys = res.data.data
                    if(!(dependencys instanceof Array)){
                        error("服务器返回的依赖列表不是数组")
                    }else{
                        for(let i = 0;i <dependencys.length;i++){
                            // 更新dataSource 依赖列表 含有多个数据
                            this.dataSource.push({
                                index:i,
                                tempState:0,
                                history:[],
                                dependency:dependencys[i],
                                suggestion:[]
                            })
                        }
                    }
                    
                }else{
                    error("服务器错误")
                }
            })
            debug("更新依赖列表")
        },
        // 获取依赖列表
        getDependencyList(){
            return this.dependencyList
        },
        // 获取数据源       
        setTempState(state){
            this.dataSource[this.index].tempState = state
        },
        async pullDependencyCode(to_index){
            this.compileError = []
            this.compileWarning = []
            // 获取code代码
            debug("更新代码" + "index:" + to_index)
            
            this.showState = CODE_SHOW
            await axios.get(url + 'code',{
                params:{
                    id:this.dataSource[to_index].dependency.id
                }
            }).then(res=>{
                if(res.status === 200){
                    this.index = to_index
                    this.code = res.data.data
                    this.showState = CODE_SHOW
                    info("更新代码成功")
                }else{
                    error("更新代码失败")
                }
            })
        },
        getDependencyCode(){
            return this.code
        },
        getShowState(){
            return this.showState
        },
        getDataSource(){
            return this.dataSource
        },
        updateCompileInfo(responseData){
            // 清除原本的警告和错误
            this.compileError = []
            this.compileWarning = []
            // 对 Warning 和 Error 进行处理
            if(responseData.data != null){
                for(let i = 0;i < responseData.data.length;i++){
                    // 拆分出行号和信息
                    let temp = responseData.data[i].split(':')
                    // 行号
                    let line = temp[0]
                    // 信息
                    let message = temp[1]
                    this.compileWarning.push({
                        line:line,
                        message:message
                    })
                }
            }
            if(responseData.msg != null && responseData.msg != ''){
                let temp = responseData.msg.split(':')
                if(temp.length == 1){
                    this.compileError.push({
                        line:0,
                        message:temp[0]
                    })
                }else{
                    let line = temp[0]
                    let message = temp[1]
                    this.compileError.push({
                        line:line,
                        message:message
                    })
                }
            }
        },
        async submitCode(){            
            // 提交代码
                info("提交代码" + this.code)
                let editor = useEditorStore()
                await axios.post(url + 'createByCode',{
                    id:this.dataSource[this.index].dependency.id,
                    code:this.code,
                    name:this.getName(),
                    description:this.getDescription(),
                    suggestion_when_check:editor.option_suggestion_when_check,
                    suggestion_when_pass:editor.option_suggestion_when_pass
                }).then(res=>{
                    if(res.status === 200){
                        info("提交代码成功")
                        if(res.data.msg == '编译成功'){
                            // 更新数据源的id 后面使用id给依赖赋值颜色
                            info("编译成功")
                            // 发送通知
                            ElNotification({
                                title: '编译成功',
                                message: '编译成功',
                                type: 'success',
                                duration: 1000
                            })
                            info("更新数据源的id" + res.data.state)
                            this.dataSource[this.index].dependency.id = res.data.state
                            this.updateCompileInfo(res.data)
                        }else{
                            info("编译失败")
                            // 发送通知
                            ElNotification({
                                title: '编译失败',
                                message: '编译失败',
                                type: 'error',
                                duration: 1000
                            })
                            this.updateCompileInfo(res.data)
                        }
                    }else{
                        error("提交代码失败")
                    }
                })
        },
        newDependency(){
            // 新建依赖
            info("新建依赖")
            let Utils = useUtilsStore()
            // 新增一个dataSource
            this.dataSource.push({
                index:this.dataSource.length,//最新的
                tempState:0, // 默认的
                history:[], // 没有
                dependency:{
                    id: null, // 不给id提供默认值
                    name:Utils.RandomName(),
                    description:Utils.RandomDescription()
                }
            })

            this.code = ''
            this.showState = CODE_SHOW
            this.index = this.dataSource.length - 1

        },
        getName(){
            return this.dataSource[this.index].dependency.name
        },
        getDescription(){
            return this.dataSource[this.index].dependency.description
        },
        deleteDependency(index){
            debug("删除依赖" + "index:" + index)
            // 本地依赖删除（未编译）
            if(this.dataSource[index].dependency.id == null){
                this.dataSource.splice(index,1)
                if(this.index == index){
                    this.checkoutDependency(0);
                }
                // 删除依赖中的数据
                this.dependencyList.splice(index,1)
            }else{
                // 服务器依赖删除（已编译）
                axios.get(url + 'delete',{
                    params:{
                        id:this.dataSource[index].dependency.id
                    }
                }).then(res=>{
                    if(res.status === 200){
                        if(res.data.state == 0){
                            info("删除依赖成功")
                            this.dataSource.splice(index,1)
                            this.dependencyList.splice(index,1)
                            // 更新index
                            if(this.index == index){
                                this.checkoutDependency(0)
                            }
                        }else{
                            error("删除依赖失败")
                        }
                    }else{
                        error("删除依赖失败")
                    }
                })
            }
        }
    }
})


export const useTransportStore = defineStore('transport',{
    state: () => ({
        // 初始值
        robot:0,
        message: ''
    }),
    actions: {
        // 提交数据
        async sendMessage(){
            // 发送数据
            let dataSource = useDataStore()
            dataSource.addRequestData(this.message)
            await axios.post(url + 'submit',{
                robot:this.robot,
                message: this.message
            }).then(res=>{ // 处理res
                if(res.status === 200){
                    info(res.data.data)
                    dataSource.addResposeData(res.data.data)
                    // 更新数据源
                    dataSource.setTempState(res.data.state)
                }else{
                    error("服务器错误")
                }
            })
            this.message = '';
        }

    }
})


let RANDOM_NAME = ['小嫦娥','孙权','小猪佩奇','曹操','孙悟空','朱棣','乾隆','张杰','小花','siri','贾维斯','小明','小红','不良帅'];

let RANDOM_DESCRIPTION = [
    '这是一个很好的人',
    '这个客服很耐心',
    '这个客服很热情',
    '事无巨细的回答',
    '即刻起兵,闪击东吴',
    '我要打十个',
    '还有谁',
    '世上无难事,只怕有心人',
    '原神,启动!',
    'pacience is key in life',
    '我想做个好人'
];

export const useUtilsStore = defineStore('utils',{
    state: () => ({}),
    actions: {
        // 根据传入的字符串封装成Transport中需要的数据
        ParseData(str){
            // 填充时间
        },
        // 随机产生名称和描述
        RandomName(){
            // 随机从数组中选取一个
            return RANDOM_NAME[Math.floor(Math.random() * RANDOM_NAME.length)]
        },
        RandomDescription(){
            return RANDOM_DESCRIPTION[Math.floor(Math.random() * RANDOM_DESCRIPTION.length)]
        }
    }
})

export const useEditorStore = defineStore('editor',{
    state: () => ({
        // editor 为编辑器实例!
        highLightLine:0,
        highLightType:'error',
        DemoCode:'', // 代码范例 指示怎么写代码
        option_suggestion_when_check:true, // 代码检查时的建议
        option_suggestion_when_pass:false, // 运行时的建议
    }),
    actions: {
        // 获取代码范例
        async getDemoCode(){
            await axios.get(url + 'DemoCode').then(res=>{
                if(res.status === 200){
                    let editor = useEditorStore()
                    editor.DemoCode = res.data
                }else{
                    error("获取代码范例失败")
                }
            })
        }
    }
})



