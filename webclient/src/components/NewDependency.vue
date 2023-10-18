<template>
  <div class="box" @click="dataSource.newDependency()">
    <el-icon><Plus /></el-icon>
    <div>新增客服</div>
  </div>
</template>

<script>
import { useDataStore } from '../stores/pinia'
export default {
    setup(){
        let dataSource = useDataStore()
        return {
            dataSource
        }
    }
}
</script>

<style scoped>
.box{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 95%;
    height: 10%;
    color: aqua;
    margin: 5px auto;
    background-color: #14145d;
    border-radius: 10px;

    text-align: center;
    line-height: 10%;
}

.box:hover{
    cursor: pointer;
    background-color: #14145d;
    box-shadow: 0 0 10px rgba(0,0,0,.5);
}

</style>