<template>
  <div>
  <el-container style='border: 1px solid #eee'>
      <el-main>
        <div style="margin-bottom: 30px">
          <div><el-tag >人数板块热力图</el-tag></div>
          <el-row style="height: 50px">
            <el-col >
              <el-tag>板块股票数量</el-tag>
              <el-input type="text" style="width: 200px"  suffix-icon="el-icon-search" v-model="stockNum"> {{stockNum}}</el-input>
              <el-button class="ml-5" type="primary" @click="filterSubmit">过滤</el-button>
            </el-col>
          </el-row>
          <el-container >
            <el-aside  width="50%">
              <div ref="dongCaiIndustryChart"   name="dongCai"></div>
            </el-aside>
            <el-main style= "padding:0px ">
              <div ref="dongCaiFlowMarketChart"  name="dongCaiFlowMarket"></div>
            </el-main>
          </el-container>

          <el-container >
            <el-aside  width="50%">
              <div ref="conceptChart" name="concept"></div>
            </el-aside>
            <el-main style= "padding:0px ">
              <div ref="conceptFlowMarketChart"  name="conceptFlowMarket"></div>
            </el-main>
          </el-container>

        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {coordinateChartHandler, hotChartHandler} from "../../fun/echartsUtil";


  export default {
    name: 'holderBlockChart',
    data() {
      return {
        res:{},
        stockNum: 0
      }
    },
    created() {
      this.$eventBus.$on( 'recommend' , (data)=>{
        fetch("http://192.168.1.6:28003/recommend/selectHolderChart/",
          {
            method: 'post',
            body: JSON.stringify(data),
            headers: {
              'Content-Type': 'application/json'
            }}).then(res =>res.json()).then(res => {
          console.log(res);
          this.res = res;
          this.extracted(res)
        })
      } )
    },
    methods: {

      extracted: function (res) {
        let dongCaiFlowMarketChartSource = [];
        let  conceptFlowMarketChartSource = [];
        dongCaiFlowMarketChartSource.push(['tenFlowHolderRatio', 'flowMarket', 'name']);
        conceptFlowMarketChartSource.push(['tenFlowHolderRatio', 'flowMarket', 'name']);

        let xAxi = res.xAxi;
        let dongCaiIndustryRightAxi = [];
        let dongCaiIndustryLeftAxi = [];
        let conceptRightAxi = [];
        let conceptLeftAxi = [];
        let dongCaiIndustryData = [];
        let conceptData = [];

        for (let i = 0; i < res.dongCaiIndustry.length; i++) {
          let datum = res.dongCaiIndustry[i];
          dongCaiIndustryLeftAxi.push(datum.name);
          dongCaiIndustryRightAxi.push(datum.count+"/"+datum.size);
          dongCaiFlowMarketChartSource.push([datum.tenFlowHolderRatio,datum.flowMarket,datum.name]);

          for (let j = 0; j < datum.data.length; j++) {
            let inData = [];
            inData.push(j, i, datum.data[j]);
            dongCaiIndustryData.push(inData);
          }
        }

        for (let i = 0; i < res.concept.length; i++) {
          let datum = res.concept[i];
          conceptLeftAxi.push(datum.name);
          conceptRightAxi.push(datum.count+"/"+datum.size);
          conceptFlowMarketChartSource.push([datum.tenFlowHolderRatio,datum.flowMarket,datum.name]);

          for (let j = 0; j < datum.data.length; j++) {
            let inData = [];
            inData.push(j, i, datum.data[j]);
            conceptData.push(inData);
          }
        }
        console.info(dongCaiFlowMarketChartSource);
        coordinateChartHandler("conceptFlowMarket",this.$echarts.init(this.$refs.conceptFlowMarketChart),conceptFlowMarketChartSource,'flowMarket', 'name',700);
        coordinateChartHandler("dongCaiFlowMarket",this.$echarts.init(this.$refs.dongCaiFlowMarketChart),dongCaiFlowMarketChartSource,'flowMarket', 'name',700);


        hotChartHandler("dongCai",this.$echarts.init(this.$refs.dongCaiIndustryChart), xAxi, dongCaiIndustryLeftAxi,dongCaiIndustryRightAxi, dongCaiIndustryData,-25,35,60);
        hotChartHandler("concept",this.$echarts.init(this.$refs.conceptChart),xAxi, conceptLeftAxi,conceptRightAxi, conceptData,-25,35,60);
      },
      filterSubmit: function () {
        let filterRes = {};
        filterRes['xAxi'] = this.res.xAxi;
        let dongCaiList = [];
        let conceptList = [];

        let dongCaiIndustry = this.res.dongCaiIndustry;
        let concept = this.res.concept;

        for (let i = 0; i < dongCaiIndustry.length; i++) {
            if(dongCaiIndustry[i].count > this.stockNum){
              dongCaiList.push(dongCaiIndustry[i]);
            }
        }

        for (let i = 0; i < concept.length; i++) {
          if(concept[i].count > this.stockNum){
            conceptList.push(concept[i]);
          }
        }
        filterRes['dongCaiIndustry'] = dongCaiList;
        filterRes['concept'] = conceptList;
        this.extracted(filterRes);
      }
    }
  }
</script>


