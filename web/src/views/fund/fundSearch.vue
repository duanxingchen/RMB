<template>
  <div style="margin: 10px 0">
    <el-input type="text" style="width: 200px" placeholder="私募名称" suffix-icon="el-icon-search" v-model="input"> {{input}}</el-input>
    <el-button class="ml-5" type="primary" @click="submit">搜索</el-button>
    <el-button class="ml-5" type="primary" @click="submit">添加私募</el-button>

  </div>
</template>


<script>

   export default {
     name: "fundSearch",
        data() {
          return {
            input: "国赞"
          }
        },
     methods:{
        submit: function () {
          if (this.input !== null || this.input !== ''){
            this.$eventBus.$emit('fund', this.input);
          }
        }
      }
    }
</script>

<style scoped>

</style>
