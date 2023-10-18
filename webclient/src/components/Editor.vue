<template>
    <codemirror
    v-model="dataSource.code"
    placeholder="编写你的代码..."
    :style="{ height: '100%', width: '100%' }"
    :autofocus="true"
    :indent-with-tabs="true"
    :tab-size="2"
    :extensions="extensions"
    @ready="handleReady"  
  />
  
</template>

<script>
  import { defineComponent, ref, shallowRef } from 'vue'
  import { useDataStore } from '../stores/pinia'
  
  import { Codemirror } from 'vue-codemirror'
  import { javascript } from '@codemirror/lang-javascript'
  import { oneDark } from '@codemirror/theme-one-dark'
  
  


  export default defineComponent({
    components: {
      Codemirror
    },
    computed:{
      
    },
    setup() {
      const extensions = [javascript(), oneDark]

      const view = shallowRef()
      
      const handleReady = (payload) => {
        view.value = payload.view
      }  


      let dataSource = useDataStore()

      return {
        extensions,
        handleReady,
        dataSource
      }
    }
  })
</script>

<style>

</style>