<template>
  <div class="editorwindows">
    <!-- 可修改 -->
    <div class="name">{{ dataSource.getName() }}</div>
    <div class="main">
      
      <!-- 主要根据这个的消息来确定要不要更新上面的name和description -->
      <div class="editorpart">
        <editor/>
      </div>
      
      <div class="rightside">
        <div class="description">{{ dataSource.getDescription() }}</div>
        <el-button class="submit" type="primary" @click="dataSource.submitCode()">提交</el-button>
      </div>
    </div>
    
    <div class="info">
      <div class="infometa warning" v-for="(item,index) in dataSource.warning" :key="index">{{ item }}</div>
      <div class="infometa error" v-show="dataSource.error != null && dataSource.error != ''"> {{ dataSource.error }} </div>
    </div>

  </div>
</template>

<script>
import Editor from './Editor.vue'
import { useDataStore } from '../stores/pinia'
export default {
  components: { Editor },    
  setup() {
    let dataSource = useDataStore()
    return{
      dataSource
    }
  }
}
</script>

<style scoped>
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
}

.main{
  height: 80%;
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.description{
  height: 90%;
  width: 100%;
}

.editorpart{
  height: 100%;
  width: 90%;
  border-radius: 20px;
}

.info{
  height: 15%;
  width: 100%;
  overflow: hidden;
  border: 2px solid black;
  border-radius: 10px;



}

.infometa{
  height: 20%;
  width: 100%;
  text-align: left;
  line-height: normal;
  padding-left: 5px;

  border-top: 2px solid black;
  border-left: 1px solid #ccc;
  
}

.warning{
  background-color: #f0ad4e;
}

.error{
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
}

</style>