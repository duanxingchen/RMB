<template>
  <div>
  <el-container style='border: 1px solid #eee'>
      <el-main>
        <div style="margin-bottom: 30px">
          <el-row>
            <el-col :span="12"><el-descriptions title="板块异常"></el-descriptions></el-col>
          </el-row>
          <div ref="tranChart" style="width:100%; height:400px"></div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {dateFormat,timeFormat} from "../../fun/timeUtil.js";

  export default {
    name: 'plateBlockChart',
    data() {
      return{
        tranData :[]
      }
    },
    created() {
      this.$eventBus.$on( 'monitor' , (blackName)=>{
        fetch("http://127.0.0.1:28003/monitor/selectPlateBlock/" +blackName)
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tranData = res;
          this.filterHandler();
        })
      } )
    },
    methods: {
      filterHandler: function () {
        const chart = this.$refs.tranChart;
        const myChart = this.$echarts.init(chart);
        let timeData = [];
        let plateNum = [];
        let upPlate = [];

        for (let i = 0; i < this.tranData.length; i++) {
          let re = this.tranData[i];
          timeData.push(dateFormat(re.reportDate));
          plateNum.push(re.plateNum);
          upPlate.push(re.upPlate);
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
            data: ['异动'],
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
              xAxisIndex: [0]
            }
          ],
          grid: [
            {
              left: 60,
              right: 50,
              height: '70%'
            },
            {
              left: 60,
              right: 50,
              height: '70%'
            }
          ],
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              axisLine: {onZero: true},
              data: timeData
            }
          ],
          yAxis: [
            {
              name: '异动次数',
              type: 'value'
            }
          ],
          series: [
           /* {
              name: '异动全部',
              type: 'bar',
              symbolSize: 1,
              data: plateNum
            },*/
            {
              name: '涨异动',
              type: 'line',
              symbolSize: 1,
              data: upPlate
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

