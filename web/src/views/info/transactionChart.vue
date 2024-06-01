<template>
  <div>
  <el-container style='border: 1px solid #eee'>
      <el-main>
        <div style="margin-bottom: 0px">
          <el-row>
            <el-col :span="12"><el-descriptions title="能动变化"></el-descriptions></el-col>
            <el-col :span="12">
              <el-button type="text"  @click="filterHandlerDay">日</el-button>
              <el-button type="text"  @click="filterHandler5Min">5分钟</el-button>
              <el-button type="text"  @click="filterHandler15Min">15分钟</el-button>
              <el-button type="text"  @click="filterHandler30Min">30分钟</el-button>
              <el-button type="text"  @click="filterHandler60Min">60分钟</el-button>
            </el-col>

          </el-row>
          <div ref="tranChart" style="width:100%; height:1000px"></div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {dateFormat,timeFormat} from "../../fun/timeUtil.js";

  export default {
    name: 'transactionChart',
    data() {
      return{
        tranData :[]
      }
    },
    created() {


      this.$eventBus.$on( 'info' , (code)=>{
        fetch("http://192.168.1.6:28003/info/transaction/" +code)
          .then(res =>res.json()).then(res => {
          console.log(res);
          console.log("transaction");
          this.tranData = res;
          this.filterHandler("101");
        })
      } )
    },
    methods: {
      filterHandlerDay(){
        this.filterHandler("101");
      },
      filterHandler5Min(){
        this.filterHandler("5");
      },
      filterHandler15Min(){
        this.filterHandler("15");
      },
      filterHandler30Min(){
        this.filterHandler("30");
      },
      filterHandler60Min(){
        this.filterHandler("60");
      },
      filterHandler: function (dataType) {
        const chart = this.$refs.tranChart;
        const myChart = this.$echarts.init(chart);
        let timeData = [];
        let priceData = [];
        let energyData = [];
        let energyDo = [];
        let priceRateData = [];
        let plateData = [];
        let plateUpData = [];

        for (let i = 0; i < this.tranData.length; i++) {
          let re = this.tranData[i];
          /**
           *取不复权*/
          if (re.dataType === dataType && re.reinstatement === "0") {
            if (dataType === "101") {
              timeData.push(dateFormat(re.reportDate));
            } else {
              timeData.push(timeFormat(re.reportDate));
            }
            plateData.push(re.plateCount);
            plateUpData.push(re.plateUpCount);
            energyDo.push(re.energyDo);
            energyData.push(re.energy);
            priceRateData.push(re.t3);
          }

          /**
          * 价格取前复权*/
          if (re.dataType === dataType && re.reinstatement === "1") {
            priceData.push(re.tclose);
          }
        }


        let option = {
          title: {
            text: '',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              animation: false
            }
          },
          legend: {
            data: ['价格','异动全部','异动涨','能动'],
            left: 10
          },
          toolbox: {
            feature: {
              dataZoom: {
                yAxisIndex: 'none'
              },
              restore: {},
              saveAsImage: {}
            }
          },
          axisPointer: {
            link: [
              {
                xAxisIndex: 'all'
              }
            ]
          },
          dataZoom: [
            {
              show: true,
              realtime: true,
              start: 0,
              end: 100,
              xAxisIndex: [0, 1, 2,3]
            }
          ],
          grid: [
            {
              left: 60,
              right: 50,
              height: '50%'
            },
            {
              left: 60,
              right: 50,
              height: '50%'
            },
            {
              left: 60,
              right: 50,
              top: '65%',
              height: '10%'
            }
            ,
            {
              left: 60,
              right: 50,
              top: '85%',
              height: '10%'
            }
          ],
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              axisLine: {onZero: true},
              data: timeData
            },
            {
              gridIndex: 1,
              type: 'category',
              boundaryGap: false,
              axisLine: {onZero: true},
              data: timeData,
              position: 'left'

            },
            {
              gridIndex: 2,
              type: 'category',
              boundaryGap: false,
              axisLine: {onZero: true},
              data: timeData,
              position: 'left'

            },
            {
              gridIndex: 3,
              type: 'category',
              boundaryGap: false,
              axisLine: {onZero: true},
              data: timeData,
              position: 'left'

            }

          ],
          yAxis: [
            {
              name: '价格',
              type: 'value'
            },
            {
              gridIndex: 1,
              name: '异动',
              type: 'value',
              position: 'right'
            },
            {
              gridIndex: 2,
              name: '能动(振幅/换手率)',
              type: 'value'
            },
            {
              gridIndex: 3,
              name: '价格波动率(增幅-|涨跌幅|)',
              type: 'value'
            }
          ],
          series: [
            {
              name: '价格',
              type: 'line',
              symbolSize: 1,
              data: priceData
            },
            {
              name: '异动全部',
              type: 'bar',
              xAxisIndex: 1,
              yAxisIndex: 1,
              symbolSize: 1,
              data: plateData
            },
            {
              name: '异动涨',
              type: 'bar',
              xAxisIndex: 1,
              yAxisIndex: 1,
              symbolSize: 1,
              data: plateUpData
            },
            {
              name: '能动(价格振幅/换手率)',
              type: 'line',
              xAxisIndex: 2,
              yAxisIndex: 2,
              symbolSize: 1,
              data: energyData
            },
            {
              name: '能动(价格振幅/换手率)',
              type: 'line',
              xAxisIndex: 2,
              yAxisIndex: 2,
              symbolSize: 1,
              data: energyDo
            },
            {
              name: '价格波动率(增幅-|涨跌幅|)',
              type: 'bar',
              xAxisIndex: 3,
              yAxisIndex: 3,
              symbolSize: 1,
              data: priceRateData
            }
          ]
        };

        myChart.setOption(option);
        window.addEventListener('resize', function () {
          myChart.resize();
        });
      }
    }
  }
</script>

