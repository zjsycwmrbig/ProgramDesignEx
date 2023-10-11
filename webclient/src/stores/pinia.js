import { defineStore } from "pinia";
import axios from "axios";
import { debug,info,warning,error } from "./LOG"; 
import { MESSAGE_REQUEST_TYPE, MESSAGE_RESPOSE_TYPE } from "./Constant";
export var url = 'http://localhost:8080/'


export const useTryStore = defineStore('try',{
    state: () => ({
        try1: 'try1',
    })
})

export const useDataStore = defineStore('data',{
    state: () => ({ // 初始值
        // 只读值
        index: 0,
        data: [[]], // 暂存历史数据
        state:[], // 暂存状态
        dependencyList:[],
        dataSource:[]
/*
        data:[
            {
                index
                TempState: 临时状态 从相应生成器中获得
                history:[] 
                dependency:{
                    id
                    name
                    description
                } 
            },
        ]
*/ 
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
            if(to_index < this.dataSource.length){
                await axios.get(url + 'checkout',{
                    id:this.dataSource[to_index].dependency.id,
                    state:this.dataSource[to_index].tempState
                }).then(res=>{
                    if(res.status === 200){
                        info("切换数据源成功")
                        // 更新index的暂存响应值
                        this.index = to_index
                    }else{
                        error("切换数据源失败")
                    }
                })
            }else{
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
                            // 更新dataSource
                            this.dataSource.push({
                                index:i,
                                tempState:0,
                                history:[],
                                dependency:dependencys[i]
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



export const Utils = defineStore('utils',{
    state: () => ({}),
    actions: {
        // 根据传入的字符串封装成Transport中需要的数据
        ParseData(str){
            // 填充时间
        }
    }
})