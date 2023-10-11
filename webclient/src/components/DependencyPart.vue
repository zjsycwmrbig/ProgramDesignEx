<template>
  <div class="box" @click="dataSource.checkoutDependency()">
    <div class="description">
      <div class="name">{{ dependency.name }}</div>
      <div class="description">{{ dependency.description }}</div>
    </div>
    <div class="option">
      <el-icon><Star /></el-icon>
    </div>
  </div>
</template>

<script>
import { useDataStore } from '../stores/pinia'

export default {
  props:['dependency'],
  setup(props){
    let dependency = props.dependency
    let dataSource = useDataStore()
    // 根据dependency选择装配,更新historyData?
    
    
    return {
      dependency,
      dataSource
    }
  }
}
</script>

<style scoped>
    .box{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 95%;
        height: 10%;
        margin: 5px auto;
        background-color: #14145d;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,.1);
        padding: 2%;
    }

    .box:hover{
        cursor: pointer;
        background-color: #14145d;
        box-shadow: 0 0 10px rgba(0,0,0,.5);
    }

    .description{
      display: flex;
        flex-direction: column;
        justify-content: space-between;  
      width: 80%;
        height: 100%;
        
    }
    .option{
        width: 20%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
    .name{
      height: 70%;
      color: #fff;
    }
    .description{
      height: 30%;
      color: #ccc;
    }
</style>