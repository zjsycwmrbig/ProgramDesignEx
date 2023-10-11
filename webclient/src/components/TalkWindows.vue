<template>
  <div class="window">
    <!-- 百分之90 -->
    
    <div class="talks">
      <!-- 大小固定，但是其中的条目不固定 -->
      <talk-record v-for="(item,index) in messageList" :message="item" :key="index"/>
    </div>
    
    <!-- 百分之10 -->
    <div class="input">
      <!-- 输入框 -->
        <div class="text">
          <el-input
            v-model="trans.message"
            autofocus="true"
            class="inputbox"
            @keydown.enter="trans.sendMessage()"
          />      
        </div>
    </div>
    
  </div>
</template>

<script>
import { useDataStore, useTransportStore } from '../stores/pinia';
import TalkRecord from './TalkRecord.vue';
export default {
  components: { TalkRecord },
  computed:{
    messageList(){
      return this.dataSource.getHistoryData();
    }
  },
  setup() {
    let trans = useTransportStore();
    let dataSource = useDataStore();
    return { 
      trans,
      dataSource
    };
  }
}
</script>

<style scoped>
  .window{
    height: 100%;
    width: 100%;
  }

  .talks{    

    margin: 3px auto;
    width: 100%;
    height: 90%;
    border: #000;
    background-color: #ccc;
  }

  .input{
    position: relative;
    width: 100%;
    height: 10%;
    padding: 1%;
  }
  
  .text{
    position: absolute;
    top:50%;
    left: 50%;
    transform: translate(-50%,-50%);
    width: 80%;
    height: 60%;
  }

  .inputbox{
    width: 100%;
    height: 100%;
  }

</style>