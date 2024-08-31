<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="股东人数变化">
            <el-descriptions-item label="报告时间" v-bind:value = reportDate >{{reportDate}}</el-descriptions-item>
            <el-descriptions-item label="股东人数" v-bind:value = holderNum>{{holderNum}}</el-descriptions-item>
            <el-descriptions-item label="私募" v-bind:value = holderName>{{holderName}}</el-descriptions-item>

            <el-descriptions-item label="当前价格" v-bind:value = price>{{price}}</el-descriptions-item>
            <el-descriptions-item label="报告时间价格" v-bind:value = noticePrice>{{noticePrice}}</el-descriptions-item>
            <el-descriptions-item label="庄家建仓成本" v-bind:value = makersCost>{{makersCost}}</el-descriptions-item>
            <el-descriptions-item label="庄家盈利" v-bind:value = priceRatio>{{priceRatio}}</el-descriptions-item>

            <el-descriptions-item label="sort12" v-bind:value = sort12>{{sort12}}</el-descriptions-item>
            <el-descriptions-item label="sort13" v-bind:value = sort13>{{sort13}}</el-descriptions-item>
            <el-descriptions-item label="sort14" v-bind:value = sort14>{{sort14}}</el-descriptions-item>
            <el-descriptions-item label="sort15" v-bind:value = sort15>{{sort15}}</el-descriptions-item>
            <el-descriptions-item label="sort16" v-bind:value = sort16>{{sort16}}</el-descriptions-item>
            <el-descriptions-item label="总市值" v-bind:value = market>{{market}}</el-descriptions-item>
            <el-descriptions-item label="前十大股东占总流通股本持股比例" v-bind:value = tenFlowHolderRatio>{{tenFlowHolderRatio}}</el-descriptions-item>
            <el-descriptions-item label="实际流通值" v-bind:value = flowMarket>{{flowMarket}}</el-descriptions-item>
            <el-descriptions-item label="庄家持股比例" v-bind:value = makersRatio>{{makersRatio}}</el-descriptions-item>
            <el-descriptions-item label="散户持股比例" v-bind:value = sanHuRatio>{{sanHuRatio}}</el-descriptions-item>
            <el-descriptions-item label="庄家和散户比例" v-bind:value = makerVsSanHuRatio>{{makerVsSanHuRatio}}</el-descriptions-item>
            <el-descriptions-item label="大行业" v-bind:value = industryBig>{{industryBig}}</el-descriptions-item>
            <el-descriptions-item label="中行业" v-bind:value = industryMiddle>{{industryMiddle}}</el-descriptions-item>
            <el-descriptions-item label="东财行业" v-bind:value = dongCaiIndustry>{{dongCaiIndustry}}</el-descriptions-item>
          </el-descriptions>
        </div>

      </el-main>

    </el-container>
  </div>
</template>

<script>
  import {dateFormat} from "../../fun/timeUtil.js";

  export default {
    name: 'holder',
    data() {
      return {
        reportDate: '',
        holderNum: '',
        price: '',
        noticePrice: '',
        priceRatio:'',
        sort12: '',
        sort13: '',
        sort14: '',
        sort15: '',
        sort16: '',
        market: '',
        flowMarket: '',
        makersRatio: '',
        sanHuRatio: '',
        makersCost: '',
        tenFlowHolderRatio: '',
        makerVsSanHuRatio: '',
        holderName:'',
        industryBig: '',
        industryMiddle: '',
        dongCaiIndustry: ''
      }
    },
    created() {
      this.$eventBus.$on( 'info' , (code)=>{
        fetch("http://127.0.0.1:28003/info/holder/" +code)
          .then(res =>res.json()).then(res => {
          console.log(res);
            this.reportDate = dateFormat(res.reportDate)
            this.holderNum = res.holderNum
            this.price = res.price
            this.priceRatio = res.priceRatio
            this.holderName = res.holderName
            this.noticePrice = res.noticePrice
            this.sort12 = res.sort12
            this.sort13 = res.sort13
            this.sort14 = res.sort14
            this.sort15 = res.sort15
            this.sort16 = res.sort16
            this.market = res.market
            this.flowMarket = res.flowMarket
            this.makersRatio = res.makersRatio
            this.sanHuRatio = res.sanHuRatio
            this.makersCost = res.makersCost
            this.tenFlowHolderRatio = res.tenFlowHolderRatio
            this.makerVsSanHuRatio = res.makerVsSanHuRatio

            this.industryBig = res.industryBig
            this.industryMiddle = res.industryMiddle
            this.dongCaiIndustry = res.dongCaiIndustry
        })
      } )
    }
  }
</script>

