<template>
  <div style="margin: 10px 0">
    <el-input type="text" style="width: 200px" placeholder="名称或者代码" suffix-icon="el-icon-search" v-model="input"> {{input}}</el-input>
    <el-button class="ml-5" type="primary" @click="submit">搜索</el-button>
    <el-button class="ml-5" type="primary" @click="addStock">添加推荐</el-button>
  </div>
</template>

<script>

   export default {
     name: "handicapSearch",
        data() {
          return {
            input: "002003"
          }
        },
     methods:{
        submit: function () {
          fetch("http://192.168.1.6:28003/info/securityCode/all")
            .then(res => res.json()).then(res => {
            console.log(res);
            for (let i = 0; i < res.length; i++) {
              let re = res[i];
              if(this.input === re.name || this.input === re.code){
                this.$store.commit('setCode', re.code);
                this.$store.commit('setName', re.name);
                console.log(this.$store.state.name);
                this.$eventBus.$emit('handicap', re.code);
                break;
              }
            }
          })

        },
        addStock: function () {
          fetch("http://192.168.1.6:28003/recommend/addStock",{
            method: 'PUT',
            headers:{"content-type":"application/json"},
            body:JSON.stringify({
              code:this.input
            })
          })
            .then(res => console.log(res))
       }
      }
    }
</script>

<style scoped>

</style>
