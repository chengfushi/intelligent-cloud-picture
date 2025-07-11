<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<script setup lang="ts">
import BasicLayout from './layous/BasicLayout.vue'
import {useLoginUserStore} from '@/stores/useLoginUserStore'

const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()
</script>
<style>
#app {
}
</style>
