<template>
  <div>
       <date-picker/>
    <filter-industry :fatherRes="res"/>
    <el-container style='border: 1px solid #eee'>
    <el-aside width="50%" >
      <div><el-tag size="medium">涨跌幅热力图</el-tag></div>
      <div ref="dongCaiIndustryPchgChart" name="dongCai" ></div>
      <div ref="conceptPchgChart" name="concept" ></div>
    </el-aside>
      <el-main style= "padding:0px">
          <div><el-tag size="medium">换手率幅热力图</el-tag></div>
          <div ref="dongCaiIndustryTurnOverChart" name="dongCai" ></div>
          <div ref="conceptTurnOverChart" name="concept" ></div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {hotChartHandler,changeXtoDate} from "../../fun/echartsUtil";
  import DatePicker from "../layout/datePicker";
  import FilterIndustry from "../layout/filterIndustry";

  export default {
    name: 'tranHotChart',
    components: {FilterIndustry, DatePicker},
    provide(){
      return {
        submit : this.submit,
        dongCaiExtracted:this.dongCaiExtracted,
        conceptExtracted:this.conceptExtracted
      };
    },
    data() {
      return {
        res:{}
      }
    },
    created() {

    },
    methods: {
      submit(value){
        fetch("http://localhost:28003/tran/selectTranHotChartByDate/",
          {
            method: 'post',
            body: JSON.stringify(value),
            headers: {
              'Content-Type': 'application/json'
            }}).then(res =>res.json()).then(res => {
          console.log(res);
          this.res = res;
          let xAxi = changeXtoDate(res.xAxi);
          this.dongCaiExtracted(xAxi,res.dongCaiIndustry);
          this.conceptExtracted(xAxi,res.concept);
        })
      },
      handler(res) {
        let count = [];
        let yAxi = [];
        let pchgData = [];
        let turnOverData = [];

        for (let i = 0; i < res.length; i++) {
          let datum = res[i];
          yAxi.push(datum.name);
          count.push(datum.count);

          for (let j = 0; j < datum.pchgData.length; j++) {
            let inData = [];
            inData.push(j, i, datum.pchgData[j]);
            pchgData.push(inData);
          }

          for (let j = 0; j < datum.turnOverData.length; j++) {
            let inData = [];
            inData.push(j, i, datum.turnOverData[j]);
            turnOverData.push(inData);
          }

        }
        return {yAxi,count,pchgData,turnOverData};
      },

      dongCaiExtracted: function (xAxi,res) {
        let {yAxi,count,pchgData,turnOverData} = this.handler(res);

        hotChartHandler("dongCai",this.$echarts.init(this.$refs.dongCaiIndustryPchgChart), xAxi, yAxi,count, pchgData,-2,4,10);
        hotChartHandler("dongCai",this.$echarts.init(this.$refs.dongCaiIndustryTurnOverChart), xAxi, yAxi,count, turnOverData,1,7,10);
      },

      conceptExtracted: function (xAxi,res) {
        let {yAxi,count,pchgData,turnOverData} = this.handler(res);
         hotChartHandler("concept",this.$echarts.init(this.$refs.conceptPchgChart),xAxi, yAxi,count, pchgData,-2,7,10);
         hotChartHandler("concept",this.$echarts.init(this.$refs.conceptTurnOverChart),xAxi, yAxi,count, turnOverData,1,7,10);
      }
    }
  }
</script>


