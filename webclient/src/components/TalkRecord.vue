<template>
  <div :class="talk_type">
     {{ messageStr }}
  </div>
</template>

<script>
import { MESSAGE_REQUEST_TYPE } from '../stores/Constant';
import { info } from '../stores/LOG';
export default {
    // 获得消息列表
    props:['message'],
    setup(props) {
        // 样式设定
        let talk_type = 'response';
        if(props.message.type == MESSAGE_REQUEST_TYPE){
            talk_type = 'request';
        }else{
            talk_type = 'response';
        }
        
        // 返回数据 临时的,之后是data传输到meta中进一步处理
        let messageStr = props.message.data
        return { 
          talk_type,
          messageStr
        };
    }
}
</script>

<style scoped>

.request{
  margin: 3px auto;
  width: 100%;
  padding: 2%;
  border: 2px #000;
  border-radius: 3px;
  background-color: #fff5eb;
  text-align: right;    
  color: #000;
}

.response{
  margin: 3px auto;
  width: 100%;
  padding: 2%;
  border: 2px #000;
  border-radius: 3px;
  background-color: #e1eaff;
  text-align: left;
  color: #000;
}


</style>