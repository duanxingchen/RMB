<template>
  <div class="content menu">
    <div class="menu_com" :style="{height:scrollHeight+'px'}">
      <el-col :span="24">
        <el-menu :default-active="activeIndex" class="el-menu-vertical-demo" :default-openeds="defalutIndex">
          <Menu :listAll="listAll"></Menu>
        </el-menu>
      </el-col>
    </div>
  </div>
</template>

<script>

  import { menuJson } from './data'
  import Menu from "./Menu"; //调用js文件中的菜单数据
  export default {
    name: "Menus",
    components: { Menu },
    data() {
      return {
        scrollHeight: 400,
        listAll: [],
        activeIndex: "-1",
        defalutIndex: []
      }
    },
    created() {
      //设置点击菜单的索引，可以使得刷新后菜单仍保持原来查看的页面
      this.activeIndex = String(this.$route.query.index);
      this.listAll = menuJson() //通过调用函数menuJson,获取菜单
    },
    watch: {
      $route(to, from) {
        this.activeIndex = this.$route.query.index;
      }
    },
  }
</script>

