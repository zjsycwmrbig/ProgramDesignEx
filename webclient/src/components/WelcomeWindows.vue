<template>
    <div class="welcome" v-show="dataSource.showState == WELCOME_SHOW">
        <div class="welcome-title">
            <h1>欢迎使用</h1>
        </div>
        <div class="welcome-content">
            <p>这是一个客服机器人</p>
        </div>
    </div>

</template>

<script>
import { WELCOME_SHOW } from '../stores/Constant';
import { useDataStore } from '../stores/pinia';

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
    .welcome{
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        color: black;
        margin: 3px;
    }

    .welcome-title{
        height: 5%;
        width: 100%;
        text-align: center;
    }

    .welcome-content{
        height: 90%;
        width: 100%;
        display: flex;
        justify-content: space-between;
        flex-direction: column;
    }

    .welcome-content p{
        margin: 5px;
    }
</style>