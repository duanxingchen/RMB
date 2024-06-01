<template>
  <div>
    <date-picker/>
    <el-container style='border: 1px solid #eee'>
    <el-aside width="40%" style= "padding:10px ">
      <div><el-tag size="medium">涨跌幅热力图</el-tag></div>
      <div ref="pchgChart"  name="stock" ></div>
    </el-aside>
      <el-main style= "padding:10px ">
          <div><el-tag size="medium">换手率幅热力图---实际交易市值</el-tag></div>
        <el-row >
            <el-col :span="16">
              <el-container>
                <el-main style= "padding:0px ">
                  <div ref="turnOverChart" name="stock" ></div>
                </el-main>
              </el-container>
            </el-col>
          <el-col :span="8">
            <el-container>
              <div ref="flowMarketChart" name="flowMarket" ></div>
            </el-container>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {hotChartHandler,changeXtoDate,coordinateChartHandler} from "../../fun/echartsUtil";
  import DatePicker from "../layout/datePicker";

  export default {
    name: 'holderStockChart',
    provide(){
      return {
        submit : this.submit
      };
    },
    components: {DatePicker},
    data() {
      return {
        tableRes:{},
        res:{},
        oderYAxi:{}
      }
    },
    created() {
      this.$eventBus.$on( 'holderStockChart' , (res)=>{
        this.tableRes = res;
      } )

    },
    methods: {
      flowMarketExtracted: function () {
        let source = [];
        source.push(['tenFlowHolderRatio', 'flowMarket', 'name']);

        let nameHashMap = {};
        for (let i = 0; i < this.tableRes.length; i++) {
          let tableRe = this.tableRes[i];
          nameHashMap[tableRe.name] = [tableRe.tenFlowHolderRatio, tableRe.flowMarket, tableRe.name];
        }
        for (let i = 0; i < this.oderYAxi.length; i++) {
          console.info(nameHashMap[this.oderYAxi[i]]);
          source.push(nameHashMap[this.oderYAxi[i]])
        }

        coordinateChartHandler("flowMarket",this.$echarts.init(this.$refs.flowMarketChart),source,'flowMarket', 'name',500);
      },

      submit(dates){
        let form ={};
        let codes = [];
        for (let i = 0; i < this.tableRes.length; i++) {
          let re = this.tableRes[i];
          codes.push(re.code);
        }

        form['codes'] = codes;
        form['dates'] = dates;
        console.info(form);


        fetch("http://192.168.1.6:28003/tran/selectTranHotChartByCodes/",
          {
            method: 'post',
            body: JSON.stringify(form),
            headers: {
              'Content-Type': 'application/json'
            }}).then(res =>res.json()).then(res => {
          console.log(res);
          this.res = res;
          this.extracted(res);
          this.flowMarketExtracted();

        })
      },
      handler(res) {
        let pchgSum = [];
        let turnOverSum = [];

        let yAxi = [];
        let pchgData = [];
        let turnOverData = [];

        for (let i = 0; i < res.length ; i++) {
          let datum = res[i];
          yAxi.push(datum.name);
          pchgSum.push(datum.pchgSum);
          turnOverSum.push(datum.turnOverSum);


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
        return {yAxi,pchgSum,turnOverSum,pchgData,turnOverData};
      },

      extracted: function (res) {
        let xAxi = changeXtoDate(res.xAxi);

        let {yAxi,pchgSum,turnOverSum,pchgData,turnOverData} = this.handler(res.data);
        this.oderYAxi = yAxi;

        hotChartHandler("stock",this.$echarts.init(this.$refs.pchgChart), xAxi, yAxi,pchgSum, pchgData,-5,10,15);
        hotChartHandler("stock",this.$echarts.init(this.$refs.turnOverChart), xAxi, yAxi,turnOverSum, turnOverData,1,20,20);
      }
    }
  }
</script>


