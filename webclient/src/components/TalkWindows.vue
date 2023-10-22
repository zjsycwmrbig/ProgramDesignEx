<template>
  <div class="window">
    <!-- 百分之90 -->
    <div class="talks" ref="container">
      <talk-record v-for="(item,index) in messageList" :message="item" :key="index" />
    </div>
    <!-- 百分之10 -->
    <div class="input">
      <!-- 输入框 -->
        <div class="text">
          <el-input
            v-model="trans.message"
            class="inputbox"
            @keydown.enter="trans.sendMessage()"
          />      
        </div>
        <el-icon size="45"><Promotion /></el-icon>
    </div>
    
  </div>
</template>

<script>
import { useDataStore, useTransportStore } from '../stores/pinia';
import Editor from './Editor.vue';
import TalkRecord from './TalkRecord.vue';
export default {
  components: { TalkRecord, Editor },
  computed:{
    messageList(){
      return this.dataSource.getHistoryData();
    },
  },
  watch:{
    messageList:{
      deep:true,
      handler(){
        this.scrollToBottom();
      }
    }
  },
  methods:{
    scrollToBottom(){
      this.$nextTick(() => {
        this.$refs.container.scrollTop = this.$refs.container.scrollHeight;
      });
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
    background-color: #242424;
    overflow-y: auto;
  }

  .talks::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  .talks::-webkit-scrollbar-thumb {
    background-color: transparent;
    border-radius: 10px;
  }

  .talks::-webkit-scrollbar-thumb :hover{
    background-color: #c2977f;
    border-radius: 10px;
  }

  .input{
    position: relative;
    width: 100%;
    height: 10%;
    padding: 1%;
    display: flex;
    justify-content: center;
  }
  
  .text{
    width: 70%;
    height: 90%;
  }

  .inputbox{
    width: 100%;
    height: 100%;
  }

</style>