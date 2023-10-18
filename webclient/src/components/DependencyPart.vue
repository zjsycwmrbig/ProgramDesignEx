<template>
  <div :class="{box:true,defeat:dependencydata.dependency.id == null}" @click="dataSource.checkoutDependency(dependencydata.index)" @mouseenter="console.log('enter'); delete_class.delete_show = true" @mouseleave="console.log('leave');delete_class.delete_show = false">
    <div class="show">
      <div class="name">{{ dependencydata.dependency.name }}</div>
      <div class="description">{{ dependencydata.dependency.description }}</div>
    </div>

    <div class="option" @click.stop="dataSource.pullDependencyCode(dependencydata.index)">
      <el-icon size="30"><Edit /></el-icon>
    </div>
    
    <div :class="{delete:true,delete_show:delete_class.delete_show}" >
      <el-icon size="30"><Delete /></el-icon>
    </div>

  </div>
</template>

<script>
import { reactive } from 'vue'
import { useDataStore } from '../stores/pinia'
import Editor from './Editor.vue'
export default {
  components: { Editor },
  props:['dependency'],
  setup(props){
    let dependencydata = props.dependency
    let dataSource = useDataStore()
    let delete_class = reactive({
      delete:true,
      delete_show:false
    })
    // 根据dependency选择装配,更新historyData?
    
    return {
      dependencydata,
      dataSource,
      delete_class
    }
  }
}
</script>

<style scoped>
    .box{
      position: relative;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 95%;
        height: 10%;
        margin: 5px auto;
        background-color: #2a2a2a;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,.1);
        padding: 5px;
        transition: all 1s;
    }

    .defeat{
        background-color: #c2977f;
    }

    .box:hover{
        cursor: pointer;
        background-color: #14145d;
        box-shadow: 0 0 10px rgba(0,0,0,.5);
    }

    .show{
      display: flex;
      flex-direction: column;
      justify-content: space-between;  
      width: 70%;
      height: 100%;
        
    }
    .option{
        width: 20%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        margin-right: 5%;
    }
    .name{
      height: 70%;
      width: 100%;
      color: #fff;
      font-size: small;
    }
    .description{
      height: 30%;
      width: 100%;
      color: #ccc;
      font-size: smaller;
    }
    
    .delete{
      display: none;
      position:absolute;
      width: 3%;
      height: 100%;
      background-color: #c2977f;
      border-radius:  50% 0 0 50%;
      transition: all 1s;
    }
    .delete_show{
      display: flex;
      justify-content: center;
      align-items: center;
      box-shadow: 0 0 10px rgba(0,0,0,.5);
      position:absolute;
      right: 0;
      top: 0;
    }
    .delete:hover{
      color: red;
      cursor: pointer;
    }
    .delete:hover{
      width: 30%;
    }
</style>