<template>
  <div class="head">
    <h3>{{ title }}</h3>
  </div>
  
  <div class="container">
    
    <div class="aside">
      <service-list/>
    </div>
    
    <div class="main">
      <talk-windows/>
    </div>

  </div>
  
  <div class="foot"> 
    <h5>
      {{ footer }}
    </h5> 
  </div>
</template>

<script>
import ServiceList from './components/ServiceList.vue';
import TalkWindows from './components/TalkWindows.vue';
import { useTryStore } from './stores/pinia'
  export default {
  components: { TalkWindows, ServiceList },
    setup() {
        const title = '客服机器人';
        const footer = '猫猫拯救世界';
        const tryStore = useTryStore();
        const getTry = tryStore.try1;
        return { 
          getTry ,
          title,
          footer
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
    
    background-color: #ccc;
  }

  .head h3{
    color: tomato;
  }
  .container{
    position: fixed;
    top: 5%;
    left: 10%;
    
    display: flex;
    justify-content: space-between;
    width: 80%;
    height: 90%;
  }
  .container .aside{
    width: 20%;
    height: 100%;
    background-color: #ccc;
    padding : 1%;
  }

  .container .main{
    position: relative;

    width: 80%;
    height: 100%;
    background-color: #eee;
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
  }


</style>
