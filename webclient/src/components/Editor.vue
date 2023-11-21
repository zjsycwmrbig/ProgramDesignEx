<template>
    <codemirror
    v-model="dataSource.code"
    placeholder="编写你的代码..."
    ref="Mycodemirror"
    :style="{ height: '100%', width: '100%' }"
    :autofocus="true"
    :indent-with-tabs="true"
    :tab-size="2"
    :extensions="extensions"
    @ready="handleReady"  
  />
</template>

<script>
  import { defineComponent,nextTick,shallowRef, watch } from 'vue'
  import { useDataStore, useEditorStore } from '../stores/pinia'
  import { Codemirror } from 'vue-codemirror'
  import { javascript } from '@codemirror/lang-javascript'
  import { oneDark } from '@codemirror/theme-one-dark'

  
  export default defineComponent({
    components: {
      Codemirror
    },
    setup() {
      const extensions = [javascript(), oneDark]

      const view = shallowRef()
      let codeMirrorInstance,LineInstance
      const handleReady = (payload) => {
        view.value = payload.view
        // 获得 codemirror 实例
        // codeMirrorInstance = payload.container
        // console.log(payload)
        // LineInstance = codeMirrorInstance.querySelectorAll('.cm-line')
      }  
      // 得到对应行位置的实例
      
      // let highLightLineF = function(){
      //   info('高亮')
      //   let highLightData = useEditorStore()

      //   let line = highLightData.highLightLine
      //   let type = highLightData.highLightType
      //   let editor = codeMirrorInstance
      //   // console.log(editor)
      //   // console.log(view)
      //   console.log(LineInstance.length)
      //   if(line == null){
      //     return
      //   }
      //   if(type == 'error'){
      //     editor.removeLineClass(line, "background", "line-highlight-error")
      //     editor.removeLineClass(line, "background", "line-highlight-warning")
      //     editor.addLineClass(line, "background", "line-highlight-error")
      //   }else{
      //     editor.removeLineClass(line, "background", "line-highlight-error")
      //     editor.removeLineClass(line, "background", "line-highlight-warning")
      //     editor.addLineClass(line, "background", "line-highlight-warning")
      //   }
      // }

      let dataSource = useDataStore()
      // let editor = useEditorStore()
      
      // 监视 highLightLine 的变化
      // watch(() => editor.highLightLine, (newVal, oldVal) => {
      //   console.log('highLightLine changed:', newVal);
      // });

      // 监视 highLightType 的变化
      // watch(() => editor.highLightType, (newVal, oldVal) => {
      //   console.log('highLightType changed:', newVal);
      // });

      return {
        extensions,
        handleReady,
        dataSource,
      }
    }
  })
</script>

<style>
  .line-highlight-error {
    background-color: red;
    background-color: rgba(255, 0, 0, 0.1);
  }
  .line-highlight-warning {
    background-color: #ffcc00;
    background-color: rgba(255, 204, 0, 0.1);
  }


  /* Custom scrollbar style */
  .cm-scroller::-webkit-scrollbar {
    width: 0;
  }
  
</style>