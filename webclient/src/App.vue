<template>
  <div class="head">
    
    <h3>
      {{ title }}
    </h3>
    <el-icon><Comment /></el-icon>
  </div>
  
  <div class="container">
    
    <div class="aside">
      <service-list/>
    </div>
    
    <div class="main">

      <talk-windows v-show="dataSource.showState == MESSAGE_SHOW"/>
      <editor-windows v-if="dataSource.showState == CODE_SHOW"/>
      <welcome-windows v-show="dataSource.showState == WELCOME_SHOW"/>
      <!-- Editor-windows -->
      
    </div>

  </div>
  
  <div class="foot"> 
    <h5>
      {{ footer }}
    </h5> 
  </div>
</template>

<script>
import EditorWindows from './components/EditorWindows.vue';
import ServiceList from './components/ServiceList.vue';
import TalkWindows from './components/TalkWindows.vue';
import WelcomeWindows from './components/WelcomeWindows.vue';
import { useDataStore, useEditorStore, useTryStore } from './stores/pinia'
import { MESSAGE_SHOW, CODE_SHOW, WELCOME_SHOW, websocket } from './stores/Constant'
import { debug } from './stores/LOG';
import { jsonParseLinter } from '@codemirror/lang-json';
  export default {
  components: { TalkWindows, ServiceList ,EditorWindows,WelcomeWindows},
  computed:{
    
  },  
    setup() {
        const title = '客服机器人';
        const footer = '猫猫拯救世界';
        const tryStore = useTryStore();
        const getTry = tryStore.try1;
        let dataSource = useDataStore();
        let editorStore = useEditorStore();
        editorStore.getDemoCode();
        
        // 创建websocket连接
        const ws = new WebSocket(websocket);
        ws.onopen = function(){
          console.log('连接成功');
        }
        
        ws.onmessage = function(event){
          console.log(event.data);
          let wsData = JSON.parse(event.data)
          
          if(wsData.type == 'wait_response'){
            // 添加到消息队列
            dataSource.addResposeData(wsData.data);
          }else if(wsData.type == 'suggestion'){
            // 更新到建议模块
            dataSource.setSuggestionData(wsData.data);
          }else{
            dataSource.addResposeData(wsData.data);
          }
        }

        return { 
          getTry ,
          title,
          footer,
          MESSAGE_SHOW,
          CODE_SHOW ,
          WELCOME_SHOW,
          dataSource
        };
    }
}
</script>

<style scoped>
  

  .head{
    position: fixed;
    top: 0;
    left: 0;
    
    display: flex;
    
    justify-content: center;
    align-items: center;
    
    width: 100%;
    height: 5%;
    
    background-color: #242424;
    border-bottom: 1px solid #7F7C9B;
  }

  .head h3{
    color: #7F7C9B;
  }
  .container{
    position: fixed;
    top: 5%;
    left: 0%;
    
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 90%;
  }
  .container .aside{
    width: 20%;
    height: 100%;
    background-color: #242424;
  }

  .container .main{
    position: relative;

    width: 80%;
    height: 100%;
    background-color: #242424;
    padding : 1%;
  }
  .foot{
    position:fixed;
    bottom: 0;
    left: 0;

    width: 100%;
    height: 5%;

    display: flex;
    justify-content: center;
    align-items: center;
    border-top: #7F7C9B;
  }


</style>
