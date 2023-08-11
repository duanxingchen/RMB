<template>

  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar />
      <menus/>
    </el-aside>

    <el-container>
      <el-header style="font-size: 30px; border-bottom: 1px solid #ccc; line-height: 30px; display: flex">
        <infoSearch />
      </el-header>
      <el-main>
        <base-info/>
        <holder/>
        <holder-num/>
        <organization/>
        <plateStock/>
        <transactionChart/>
      </el-main>
    </el-container>
    </el-container>
</template>

<script>

import navbar from '../layout/navbar';
import holderNum from './holderNum';
import organization from './organization';
import holder from "./holder";
import baseInfo from "./baseInfo";
import infoSearch from "./infoSearch";
import transactionChart from "./transactionChart"
import plateStock from "./plateStock";
import Menus from "../layout/Menus";

  export default {
    name: 'info',
    components: {Menus, infoSearch, plateStock,navbar, holderNum, organization, holder, baseInfo,transactionChart},
    data() {
      return {

      }
    },
    created() {
      document.title = '个股信息';
      // 请求分页查询数据
      this.load()
    },
    methods: {
    }
  }
</script>

