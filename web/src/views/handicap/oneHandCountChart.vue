<template>
  <div>
  <el-container style='border: 1px solid #eee'>
      <el-main>
        <div style="margin-bottom: 30px">
          <el-row>
            <el-col :span="12"><el-descriptions title="交易统计"></el-descriptions></el-col>
          </el-row>
          <div ref="tranChart" style="width:100%; height:900px"></div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import {dateFormat,timeFormat} from "../../fun/timeUtil.js";

  export default {
    name: 'oneHandCountChart',
    data() {
      return{
        tranData :[]
      }
    },
    created() {

      this.$eventBus.$on( 'handicap' , (code)=>{
        fetch("http://192.168.1.6:28003/handicap/selectOneHandCount/" +code)
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tranData = res;
          this.handler();
        })
      } )
    },
    methods: {

      handler: function () {
        const chart = this.$refs.tranChart;
        const myChart = this.$echarts.init(chart);
        let timeData = [];
        let priceData = [];
        let oneHandTimesData = [];
        let allTimesData = [];
        let oneHandAvgTimeData = [];
        let concurrenceHandsData = [];

        for (let i = 0; i < this.tranData.length; i++) {
          let re = this.tranData[i];
          timeData.push(dateFormat(re.reportDate));
          priceData.push(re.price);
          oneHandTimesData.push(re.oneHandTimes);
          allTimesData.push(re.allTimes);
          concurrenceHandsData.push(re.concurrenceHands);
          oneHandAvgTimeData.push(re.oneHandAvgTime);
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
            data: [],
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
              xAxisIndex: [0, 1, 2]
            }
          ],
          grid: [
            {
              left: 60,
              right: 50,
              height: '25%'
            },
            {
              left: 60,
              right: 50,
              top: '38%',
              height: '25%'
            }
            ,
            {
              left: 60,
              right: 50,
              top: '70%',
              height: '25%'
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
            }
          ],
          yAxis: [
            {
              name: '价格',
              type: 'value'
            },
            {
              gridIndex: 1,
              name: '交易次数',
              type: 'value',
            },
            {
              gridIndex: 2,
              name: '集合竞价手数',
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
              name: '一手次数',
              type: 'line',
              xAxisIndex: 1,
              yAxisIndex: 1,
              symbolSize: 1,
              data: oneHandTimesData
            },
            {
              name: '总次数',
              type: 'line',
              xAxisIndex: 1,
              yAxisIndex: 1,
              symbolSize: 1,
              data: allTimesData
            }
            ,
            {
              name: '集合竞价手数',
              type: 'bar',
              xAxisIndex: 2,
              yAxisIndex: 2,
              symbolSize: 1,
              data: concurrenceHandsData
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

