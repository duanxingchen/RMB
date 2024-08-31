<template>
  <div>
  <el-container style=" border: 1px solid #eee">
    <div id="app">
      <router-view />
    </div>
      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="个股信息">
            <el-descriptions-item label="名称" v-bind:value = name >{{name}}</el-descriptions-item>
            <el-descriptions-item label="代码" v-bind:value = code>{{code}}</el-descriptions-item>
            <el-descriptions-item label="上市日期" v-bind:value = listingDate>{{listingDate}}</el-descriptions-item>
            <el-descriptions-item label="成立日期" v-bind:value = establishmentDate>{{establishmentDate}}</el-descriptions-item>
            <el-descriptions-item label="所属东财行业" v-bind:value = dongcaiIndustry>{{dongcaiIndustry}}</el-descriptions-item>
            <el-descriptions-item label="所属证监会行业" v-bind:value = securitiesIndustry>{{securitiesIndustry}}</el-descriptions-item>
            <el-descriptions-item label="区域" v-bind:value = area>{{area}}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {dateFormat} from '../../fun/timeUtil.js';

  export default {
    name: 'BaseInfo',
    data() {
      return {
        name: '',
        code: '',
        listingDate: '',
        establishmentDate: '',
        dongcaiIndustry: '',
        securitiesIndustry: '',
        area: '',
      }
    },
    created() {
      //this.$eventBus.$off( 'info' );
      this.$eventBus.$on( 'info' , (code)=>{
        fetch('http://127.0.0.1:28003/info/baseInfo/' +code)
          .then(res =>res.json()).then(res => {
            console.log(res);
            this.code = res.code;
            this.name = res.name;
          this.listingDate = dateFormat(res.listingDate);
          this.establishmentDate = dateFormat(res.establishmentDate);
          this.dongcaiIndustry = res.dongcaiIndustry;
          this.securitiesIndustry =res.securitiesIndustry;
          this.area =res.area;
        })
      } )
    },
    methods: {

    }
  }
</script>

