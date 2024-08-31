<template>
  <div>
    <date-picker/>
    <filter-industry :fatherRes="res"/>

  <el-container style='border: 1px solid #eee'>
      <el-main>
        <div style="margin-bottom: 30px">
          <el-row>
            <el-col :span="12"><el-descriptions title="板块异常"></el-descriptions></el-col>
          </el-row>

          <div><el-tag size="medium" >火箭反射(板块)</el-tag></div>
          <div ref="upDongCaiPlateChart"   name="dongCai"></div>
          <div><el-tag size="medium" >高台跳水(板块)</el-tag></div>
          <div ref="downDongCaiPlateChart"  name="dongCai" ></div>
          <div><el-tag size="medium" >全部异常(板块)</el-tag></div>
          <div ref="allDongCaiPlateChart"   name="dongCai" ></div>

          <div><el-tag size="medium" >火箭反射(概念)</el-tag></div>
          <div ref="upConceptPlateChart"  name="concept"></div>
          <div><el-tag size="medium" >高台跳水(概念)</el-tag></div>
          <div ref="downConceptPlateChart"  name="concept" ></div>
          <div><el-tag size="medium" >全部异常(概念)</el-tag></div>
          <div ref="allConceptPlateChart"   name="concept" ></div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {changeXtoDate, hotChartHandler} from "../../fun/echartsUtil";
  import DatePicker from "../layout/datePicker";
  import FilterIndustry from "../layout/filterIndustry";


  export default {
    name: 'plateBlockHotChart',
    components: {DatePicker,FilterIndustry},
    provide(){
      return {
        submit : this.submit,
        dongCaiExtracted:this.dongCaiExtracted,
        conceptExtracted:this.conceptExtracted
      };
    },
    data() {
      return {
        res:{},
        dongCaiInput:"",
        conceptInput:""
      }
    },

    methods: {
      submit(value){
        fetch("http://127.0.0.1:28003/monitor/selectPlateBlockHotChart/",
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
        let allCount = [];
        let upCount = [];
        let downCount = [];

        let yAxi = [];
        let upData = [];
        let downData = [];
        let allData = [];

        for (let i = 0; i < res.length; i++) {
          let datum = res[i];
          yAxi.push(datum.name);
          allCount.push(datum.allCount+"/"+datum.size);
          upCount.push(datum.upCount+"/"+datum.size);
          downCount.push(datum.downCount+"/"+datum.size);

          for (let j = 0; j < datum.upInt.length; j++) {
            let inData = [];
            inData.push(j, i, datum.upInt[j]);
            upData.push(inData);
          }

          for (let j = 0; j < datum.downInt.length; j++) {
            let inData = [];
            inData.push(j, i, datum.downInt[j]);
            downData.push(inData);
          }

          for (let j = 0; j < datum.allInt.length; j++) {
            let inData = [];
            inData.push(j, i, datum.allInt[j]);
            allData.push(inData);
          }
        }
        return {yAxi,upCount,downCount,allCount, upData, downData, allData};
      },
      dongCaiExtracted: function (xAxi,res) {
        let {yAxi, upCount,downCount,allCount,upData, downData, allData} = this.handler(res);

        hotChartHandler("dongCai",this.$echarts.init(this.$refs.upDongCaiPlateChart), xAxi, yAxi,upCount, upData,0,100,100);
        hotChartHandler("dongCai",this.$echarts.init(this.$refs.downDongCaiPlateChart), xAxi, yAxi,downCount, downData,0,100,100);
        hotChartHandler("dongCai",this.$echarts.init(this.$refs.allDongCaiPlateChart), xAxi, yAxi,allCount, allData,0,100,100);
      },
      conceptExtracted: function (xAxi,res) {
        let {yAxi, upCount,downCount,allCount,upData, downData, allData} = this.handler(res);

        hotChartHandler("concept",this.$echarts.init(this.$refs.upConceptPlateChart), xAxi, yAxi,upCount, upData,0,100,100);
        hotChartHandler("concept",this.$echarts.init(this.$refs.downConceptPlateChart), xAxi, yAxi,downCount, downData,0,100,100);
        hotChartHandler("concept",this.$echarts.init(this.$refs.allConceptPlateChart), xAxi, yAxi,allCount, allData,0,100,100);

      }

    }
  }
</script>


