<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar/>
    </el-aside>

    </el-container>
</template>

<script>
  import navbar from "../layout/navbar";

  export default {
    name: 'history',
    components: {navbar},

    data() {
      return {

      }
    },
    created() {
      document.title = '历史统计';
      // 请求分页查询数据
      this.load()
    },
    methods: {
      load() {
        fetch("http://127.0.0.1:28003/user/page?pageNum="+this.pageNum+"&pageSize=" + this.pageSize + "&username=" + this.username)
          .then(res => res.json()).then(res => {
          console.log(res)
          this.tableData = res.data
          this.total = res.total
        })
      }
    }
  }
</script>

