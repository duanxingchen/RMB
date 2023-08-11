import {monthDayFormat} from "./timeUtil";

export function changeStyle(elementId,heightNum,widthNum) {
  let obj = document.getElementsByName(elementId);
  for (let i = 0; i < obj.length; i++) {
    obj[i].setAttribute("style","margin: auto ; width:"+ (35*widthNum+350) +"px; height:"+ (35*heightNum+150) +"px");
  }
}

export function hotChartHandler(elementId, chart, xAxi, yAxiLeft, yAixRight, data, visualMapMin, visualMapMax, shadowBlurSize) {
  data.map(function (item) {
      return [item[1], item[0], item[2] || '-'];
    });

    let option;

    option = {
      tooltip: {
        position: 'top'
      },
      grid: {
        left:'100px',
        right:'50px'
      },
      xAxis: [{
        type: 'category',
        data: xAxi,
        splitArea: {
          show: true
        }
      },{
        type: 'category',
        data: xAxi,
        splitArea: {
          show: true
        },
        position:"top"
      }],
      yAxis: [
        {
          type: 'category',
          data: yAxiLeft,
          splitArea: {
            show: true
          },
          position:'left'
        },
        {
          type: 'category',
          data: yAixRight,
          splitArea: {
            show: true
          },
          position:'right'
        }
      ],
      visualMap: {
        min: visualMapMin,
        max: visualMapMax,
        calculable: true,
        orient: 'horizontal',
        left: 'center',
        inRange: {
          color: [
            '#08f125',
            '#7ed041',
            '#a3d174',
            '#add97b',
            '#e0f3f8',
            '#ffffbf',
            '#fee090',
            '#e37830',
            '#ec5b6d',
            '#d72750',
            '#a10632'
          ]
        }
      },
      series: [
        {
          name: 'Punch Card',
          type: 'heatmap',
          data: data,
          label: {
            show: true
          },
          emphasis: {
            itemStyle: {
              shadowBlur: shadowBlurSize,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    changeStyle(elementId,yAxiLeft.length,xAxi.length);
   chart.clear();
   chart.setOption(option);
   chart.resize();
   window.addEventListener('resize', function () {
      chart.resize();
   });
}


export function changeXtoDate(res) {
  let xAxi = [];
  for (let i = 0; i < res.length; i++) {
    xAxi.push(monthDayFormat(res[i]));
  }
  return xAxi;
}


export function coordinateChartHandler(elementId,chart,source,xDataName,yDataName,width) {

  let option;
  option = {
    dataset: {
      source: source
    },
    grid: {
      left:'100px'
    },
    xAxis: {name: xDataName},
    yAxis: {type: 'category'},
    visualMap: {
      orient: 'horizontal',
      left: 'center',
      min: 10,
      max: 100,
      text: ['低', '高'],
      // Map the score column to color
      dimension: 0,
      inRange: {
        color: ['#65B581', '#FFCE34', '#FD665F']
      }
    },
    series: [
      {
        type: 'bar',
        label:{
          show:true,
          position: 'right'
        },
        encode: {
          // Map the "amount" column to X axis.
          x: xDataName,
          // Map the "product" column to Y axis
          y: yDataName
        }
      }
    ]
  };
  let obj = document.getElementsByName(elementId);
  for (let i = 0; i < obj.length; i++) {
    obj[i].setAttribute("style","margin: auto ; width:"+width+"px; height:"+ (35*source.length+130) +"px");
  }
  chart.clear();
  chart.setOption(option);
  chart.resize();
  window.addEventListener('resize', function () {
    chart.resize();
  });
}






