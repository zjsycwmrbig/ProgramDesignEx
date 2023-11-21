<template>
  <div class="editorwindows">
    <!-- 可修改 -->
    <div class="name">{{ dataSource.getName() }}</div>
    <div class="frame">
      <div class="farme_left">
        
        <div :class="now_main_style == 0 ? 'main_normal' : 'main_full'">
          <div class="editorpart">
            <div class="full_screen" @click="full_screen()">
              <el-icon class="full_screen_icon"><FullScreen /></el-icon>
            </div>
            <div class="generate" @click="generate()">
              <el-icon><Document /></el-icon>
            </div>
            <editor/>
          </div>
        </div>
        
        <el-button class="submit" type="primary" @click="dataSource.submitCode()">提交</el-button>
      </div>
  
      <div class="info">
        
        <!-- 如果都没有，显示无错误，薄层覆盖 -->
        <div class="infoshow" v-if="dataSource.compileError.length == 0 && dataSource.compileWarning.length == 0">
          <div class="infohead">
            <h4>温馨提示</h4>
          </div>

          <div class="codedemo">
            <div class="tip">
              <el-icon><Star /></el-icon>
              <div>在左侧编写代码，点击提交按钮，即可提交代码！</div>
            </div>
            
            <div class="tip">
              <el-icon><Star /></el-icon>
              <div>点击右上角全屏按钮，可以进入沉浸式编码模式！</div>
            </div>
            
            <div class="tip">
              <el-icon><Star /></el-icon>
              <div>点击右上角生成按钮，可以自动生成实例代码！</div>
            </div>
            
            <div class="tip">
              <el-icon><Star /></el-icon>
              <div>点击下方了解更多，可以查看代码规范和语法定义！</div>
            </div>

            <div class="dog"></div>
            <div class="learn_more">
              <a href="https://fsbupteducn.feishu.cn/docx/MpfQdf4Puog15OxVH6aczqfqnte?from=from_copylink" target="_blank">了解更多</a>
            </div>
          </div>  
          <div class="Anti-piracy">
            <div class="piracyrow" v-for="(item,index) in 20" :key="index">
              <div class="paracycol" v-for="(item,index) in 10" :key="index">
                <div class="piracytext" >
                  2021211202&nbsp;&nbsp;
                </div>
              </div>
            </div>
          </div>
          <!-- 水印来一个 -->
        </div>
        <div class="compileres" v-else>
          <div class="infometa error" v-for="(item,index) in dataSource.compileError" :key="index" @click = "loc_line(item.line,'error')">第{{ item.line }}行：  {{ item.message }} </div>
          <div class="infometa warning" v-for="(item,index) in dataSource.compileWarning" :key="index" @click = "loc_line(item.line,'warning')">第{{ item.line }}行： {{ item.message }}</div>
          
          <div class="Anti-piracy">
            <div class="piracyrow" v-for="(item,index) in 20" :key="index">
              <div class="paracycol" v-for="(item,index) in 10" :key="index">
                <div class="piracytext" >
                  2021211202&nbsp;&nbsp;
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Editor from './Editor.vue'
import { useDataStore, useEditorStore } from '../stores/pinia'
import { reactive, ref, toRefs } from 'vue'
import { info } from '../stores/LOG'
export default {
  components: { Editor },
  setup() {
    let dataSource = useDataStore()
    let now_main_style =  ref(0)
    let editor = useEditorStore()

    let full_screen = function(){
      if(now_main_style.value == 0){
        now_main_style.value = 1
      }else{
        now_main_style.value = 0
      }
    }
    // 使用说明
    let DemoCode = editor.DemoCode

    // 生成实例代码
    let generate = function(){
      // 获得范例代码
      editor.getDemoCode()
      // 更新替换范例代码
      let myCode = editor.DemoCode
      myCode = myCode.replace('__name__',dataSource.getName())
      myCode = myCode.replace('__description__',dataSource.getDescription())
      // 更新到dataSource中的code
      dataSource.code = myCode
    }

    let loc_line = function(line,type){
      // 跳转到错误的位置
      editor.highLightLine = line
      editor.highLightType = type
      info('跳转到第'+line+'行',type)
    }

    // 获得范例代码
    
    
    return{
      dataSource,
      full_screen,
      now_main_style,
      loc_line,
      DemoCode,
      generate
    }
  }
}
</script>


<style scoped>

.frame{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  height: 95%;
  width: 100%;
}

.farme_left{
  width: 50%;
  border-right: 2px solid #41417d;
}

.editorwindows{
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: black;
  margin: 3px;
}
.name{
  height: 5%;
  width: 100%;
  text-align: center;
  font-weight: 900;
  font-size: 3vh;
  color: #41417d;
  border-bottom: 2px solid #41417d;
}

.main_normal{
  height: 95%;
  width: 100%;
  display: flex;
  justify-content: space-between;
}
.main_full{
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  z-index: 1000;
}

.description{
  position: relative;
  height: 90%;
  width: 100%;
}

.full_screen{
  z-index: 1000;
  position: absolute;
  top: 0;
  right: 0;
  border-radius: 3px;
  height: 5%;
  width: 5%;
  background-color: #41417d;
  color: white;
  font-size: large;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.5s;
}

.full_screen:hover{
  cursor: pointer;
  background-color: #c2977f;
}

.generate{
  z-index: 1000;
  position: absolute;
  top: 5%;
  right: 0;
  border-radius: 3px;
  height: 5%;
  width: 5%;
  background-color: #41417d;
  color: white;
  font-size: large;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.5s;
}

.generate:hover{
  cursor: pointer;
  background-color: #c2977f;
}

.editorpart{
  position: relative;
  height: 100%;
  width: 100%;
  border-radius: 20px;
}



.info{
  height: 100%;
  width: 50%;

  overflow: hidden;
  border: 2px solid #41417d;
  border-radius: 10px;
}

.infometa{
  font-size: 1.5vh;
  margin: 5px;
  border-radius: 3px;
  padding: 3px;
  position: relative;
  z-index: 100;
  transition: all 0.5s;
}

.infometa:hover{
  cursor: pointer;
  background-color: #c2977f;
}

.warning{
  z-index: 100;
  background-color: #f0ad4e;
}

.error{
  z-index: 100;
  background-color: #d9534f;
}

.rightside{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  width: 10%;
}
.submit{
  width: 100%;
  height: 5%;
  background-color: #41417d;
  border: #41417d;
  transition: all 0.5s;
  font-weight: 900;
}

.submit:hover{
  background-color: #c2977f;
}


.Anti-piracy{
  position: absolute;
  top: 0;
  left: 0;
  height: 150%;
  width: 150%;
  transform: rotate(-45deg) translateY(-10%);
  user-select: none;
}

.piracyrow{
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: space-around;
  z-index: -1;
}

.paracycol{
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: space-around;
  z-index: -1;
}

.piracytext{
  font-size: 3.5vh;
  color: #41417d;
  opacity: 0.1;
}

.infoshow{
  position: relative;
  height: 100%;
  width: 100%;
  overflow: hidden;
  background-color:transparent;
}

.infohead{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 5%;
  width: 100%;
  color: #41417d;
  font: 800 2.5vh "Microsoft YaHei";
  z-index: 100;
}

.codedemo{
  position: relative;
  border: #41417d 2px solid;
  border-radius: 10px;
  height: 90%;
  width: 100%;
  overflow: auto;
  padding: 15px;
  z-index: 100;
  white-space: pre-wrap;
  font: 800 2vh "Microsoft YaHei";
  color: #41417d;
  z-index: 100;
}

.codedemo::-webkit-scrollbar
{
  width: 10px;
  height: 10px;
}

.compileres{
  position: relative;
  height: 100%;
  width: 100%;
  overflow: hidden;
  background-color:transparent;
}

.learn_more{
  position:absolute;
  bottom: 5px;
  right: 5px;

  display: flex;
  justify-content: flex-end;
  align-items: center;
  
  height: 5%;
  color: #c2977f;
  font: 800 2vh "Microsoft YaHei";
  background-color: #c2977f;
  border-radius: 5px;
  transition: all 0.5s;
  padding: 5px;
}
.learn_more a {
  color: #41417d;
  text-decoration: none;
  transition: all 0.5s;
}
.learn_more :hover{
  cursor: pointer;
  color: #c2977f;
  background-color: #41417d;
  
}

.dog{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
  height: 50%;
  width: 50%;
  background-image: url('../assets/dog.gif');
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.tip{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin: 15px;
  border-bottom: 2px dotted #41417d;
}
</style>