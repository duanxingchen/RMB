<template>
  <div class="myDiv">
    <!-- 这里的listAll用于接收父组件传递进来的菜单列表 -->
    <template v-for="(item,i) in listAll">
      <!-- 有child就显示child的下拉型菜单，有小箭头 -->
      <el-submenu :index="item.index" :key="i" v-if="item.child.length!=0">
        <template slot="title">
          <img :src="item.img" alt="">
          <span>{{item.title}}</span>
        </template>
        <!-- 再次调用自身组件，传入子集，进行循环递归调用 -->
        <Menu :listAll="item.child"></Menu>
      </el-submenu>
      <!-- 没有child,就显示单个目录，没有小箭头 -->
      <el-menu-item :index="item.index" v-else :key="i" @click="handleSelect(item.path,item.title,item.index)">
        <span slot="title"><img :src="item.img" alt="">{{item.title}}</span>
      </el-menu-item>
    </template>
  </div>
</template>

<script>
  export default {
    name: 'Menu',
    components: {},
    props: ['listAll'],
    data() {
      return {
        realList: this.listAll,
      }
    },
    methods: {
      //设置路由跳转
      handleSelect(path, name, selfIndex) {
        this.$router.push(
          {
            path: "/" + path,
            query: {
              r_n: name,
              index: selfIndex
            }
          }
        )
      },

    },
  }
</script>
