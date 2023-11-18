<template>
  <div class="frame">
    <div :class="talk_type">
      {{ messageStr }}
    </div>

    <div :class="icon_type">
      <el-icon size="30"><Avatar /></el-icon>
    </div>
    
  </div>
  
</template>

<script>
import { MESSAGE_REQUEST_TYPE } from '../stores/Constant';

export default {
    // 获得消息列表
    props:['message'],
    setup(props) {
        // 样式设定
        let talk_type = 'response';
        let icon_type = 'icon_response';
        if(props.message.type == MESSAGE_REQUEST_TYPE){
            talk_type = 'request';
            icon_type = 'icon_request';
        }else{
            talk_type = 'response';
            icon_type = 'icon_response';
        }
        
        // 返回数据 临时的,之后是data传输到meta中进一步处理
        let messageStr = props.message.data
        return { 
          talk_type,
          icon_type,
          messageStr
        };
    }
}
</script>

<style scoped>

.frame{
  position: relative;
  width: 100%;
  height: auto;
  margin: 3px auto;
}

.request{
  margin: 3px auto;
  width: 80%;
  padding: 2%;
  border: 2px #000;
  border-radius: 3px;
  background-color: #fff5eb;
  text-align: right;    
  color: #000;
}

.response{
  margin: 3px auto;
  width: 80%;
  padding: 2%;
  border: 2px #000;
  border-radius: 3px;
  background-color: #e1eaff;
  text-align: left;
  color: #000;
}

.icon_request{
  position: absolute;
  right: 5%;
  bottom: 50%;
  transform: translate(0,50%);
  color: #fff5eb;
}

.icon_response{
  position: absolute;
  left: 5%;
  bottom: 50%;
  transform: translate(0,50%);
  color: #e1eaff;
}


</style>