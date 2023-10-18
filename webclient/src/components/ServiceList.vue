<template>
    <!-- 这是依赖的列表 用list主要是用来框住结果-->
    <div class="list">
      <dependency-part v-for="(item,index) in dependency" :key="index" :dependency="item"/>
      <new-dependency/>
    </div>
</template>

<script>
import { useDataStore } from '../stores/pinia'
import DependencyPart from './DependencyPart.vue'
import NewDependency from './NewDependency.vue'

export default {
    components:{
        DependencyPart,
        NewDependency,
    },
    computed:{
        dependency(){
            let dataSource = useDataStore()
            return dataSource.getDataSource()
        }
    },
    setup(){
        let dataSource = useDataStore()
        dataSource.pullDependencyList()
        // 先更新
        return {
         
        }
    }
}
</script>

<style scoped>
.list{
    width: 100%;
    height: 100%;
    background-color: #242424;
    box-shadow: 0 0 10px rgba(0,0,0,.1);
    padding: 5px;
    overflow: hidden;
    border-right:2px solid #7F7C9B;
}
</style>