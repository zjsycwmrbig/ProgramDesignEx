<template>
    <div class="welcome" v-show="dataSource.showState == WELCOME_SHOW">
        <div class="logo"></div>

        <div class="copy">
            <div>@Author zjs</div>
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

    .title{
        color: #414141;
    }
    .logo{
        position:absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        background-image: url('../assets/cat_head.png');
        background-size: 100% 100%;
        height: 30%;
        width: 20%;
        transition: all 0.5s;
    }
    .logo:hover{
        cursor: pointer;
        transform: translate(-50%,-50%) scale(1.1);
    }
    .copy{
        display: flex;
        justify-content: center;
        align-items: center;

        position:absolute;
        bottom: 2vh;
        right: 5vw;
        font-size: 3vh;
        
        color: #414141;
    }
</style>