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
          <div class="suggestion" v-show="suggestionList!=null&&suggestionList.length != 0">
            <!-- 显示所有的建议 -->
            <div class="suggestion_item" v-for="(item,index) in suggestionList" :key="index" @click="trans.message = item.inputTemplate">
              <div class="itemText">
                {{ item.suggestion }}
              </div>
            </div>
          </div>

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
    suggestionList(){
      return this.dataSource.getSuggestionData();
    }
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
    height: 85%;
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
    margin-top: 5%;
    display: flex;
    justify-content: center;
    background-color: #242424;
  }
  
  .text{
    position: relative;
    width: 70%;
    height: 90%;
    background-color: #242424;
  }

  .inputbox{
    width: 100%;
    height: 100%;
    background-color: #242424;
  }

  .suggestion{
    position: absolute;
    top: -3.5vh;
    left: 0;
    width: 100%;
    height: 3vh;
    z-index: 100;
    display: flex;
    justify-content: left;
    align-items: center;
  }

  .suggestion_item{
    height: 100%;
    color: white;
    font: 800;
    border-radius: 1.5vh;
    border: #c2977f 2px solid;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 15px;
    margin-right: 1vw;
    transition: all 2s;
    overflow: hidden;
  }

  .suggestion_item:hover{
    position:absolute;
    width: 100%;
    top: 0;
    left: 0;
    cursor: pointer;
    background-color: #242424;
  }

</style>