<template>
  <div>
  <el-container style='border: 1px solid #eee'>
      <el-main>
        <div style="margin-bottom: 30px">
          <div><el-tag >人数板块热力图</el-tag></div>
          <el-row style="height: 50px">
            <el-col >
              <el-tag>股票数量</el-tag>
              <el-input type="text" style="width: 200px"  suffix-icon="el-icon-search" v-model="stockNum"> {{stockNum}}</el-input>
              <el-tag>排序</el-tag>
              <el-select v-model="sortName" placeholder="请选数量">
                <el-option label="sort12" value="sort12"></el-option>
                <el-option label="sort13" value="sort13"></el-option>
                <el-option label="sort14" value="sort14"></el-option>
                <el-option label="sort15" value="sort15"></el-option>
                <el-option label="sort16" value="sort16"></el-option>
                <el-option label="sort17" value="sort17"></el-option>
                <el-option label="sort18" value="sort18"></el-option>
                <el-option label="sort19" value="sort19"></el-option>
                <el-option label="有效股票数量" value="count"></el-option>
                <el-option label="有效股票数量占比" value="rate"></el-option>
              </el-select>
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
        stockNum: 0,
        sortName: ' '
      }
    },
    created() {
      this.$eventBus.$on( 'recommend' , (data)=>{
        fetch("http://127.0.0.1:28003/recommend/selectHolderChart/",
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
        this.sortList(dongCaiList);
        this.sortList(conceptList);
        filterRes['dongCaiIndustry'] = dongCaiList;
        filterRes['concept'] = conceptList;
        this.extracted(filterRes);
      },

      sortList: function (list){
        switch (this.sortName ){
          case "sort12":
            list.sort((a, b) => a.data[0] - b.data[0]);
            break;
          case "sort13":
            list.sort((a, b) => a.data[1] - b.data[1]);
            break;
          case "sort14":
            list.sort((a, b) => a.data[2] - b.data[2]);
            break;
          case "sort15":
            list.sort((a, b) => a.data[3] - b.data[3]);
            break;
          case "sort16":
            list.sort((a, b) => a.data[4] - b.data[4]);
            break;
          case "sort17":
            list.sort((a, b) => a.data[5] - b.data[5]);
            break;
          case "sort18":
            list.sort((a, b) => a.data[6] - b.data[6]);
            break;
          case "sort19":
            list.sort((a, b) => a.data[7] - b.data[7]);
            break;
          case "count":
            list.sort((a, b) => a.count - b.count);
            break;
          case "rate":
            list.sort((a, b) => a.count/a.size - b.count/b.size);
            break;
          default:
            list.sort((a, b) => a.data[4] - b.data[4]);
        }
      }
    }
  }
</script>


