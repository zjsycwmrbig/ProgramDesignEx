<template>
    <div class="welcome">
        
        <div class="welcome-title">
            <h1>欢迎使用</h1>
        </div>

        <div class="welcome-content">
            <p>这是一个客服机器人</p>
            <p>你可以在这里编辑你的代码</p>
            <p>你可以在这里和机器人聊天</p>
            <p>你可以在这里查看你的历史记录</p>
            <p>你可以在这里查看你的依赖</p>
            <p>你可以在这里查看你的配置</p>
            <p>你可以在这里查看你的日志</p>
            <p>你可以在这里查看你的状态</p>
            <p>你可以在这里查看你的数据</p>
        </div>

    </div>

</template>

<script>
export default {
    setup(){
        return {

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