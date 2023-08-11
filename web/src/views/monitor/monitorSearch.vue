<template>
  <div style="margin: 10px 0">
    <el-input type="text" style="width: 200px" placeholder="板块名称" suffix-icon="el-icon-search" v-model="input"> {{input}}</el-input>
    <el-button class="ml-5" type="primary" @click="submit">搜索</el-button>
  </div>
</template>


<script>

   export default {
     name: "monitorSearch",
        data() {
          return {
            input: "002003"
          }
        },
     methods:{
        submit: function () {
          this.$eventBus.$emit('monitor',this.input);
        }
      }
    }
</script>

<style scoped>

</style>
