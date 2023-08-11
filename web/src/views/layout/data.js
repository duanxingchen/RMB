

export function menuJson() {
  var data = [{
    title: "元数据管理",
    img: "../../../static/img/manager.png",
    index: '1',
    child: [
      {
        "title": "元数据信息描述管理", "path": "holderCharts", "img": "../../../static/img/manager.png", "index": "1-2", "child": []
      },
      {
        "title": "元数据分组定义管理", "path": "main/02/007", "img": "../../../static/img/manager.png", "index": "1-3", "child": []
      },
      {
        "title": "元数据信息管理", "path": "main/02", "img": "../../../static/img/manager.png", "index": "1-1", "child": [
          { "title": "采集元数据", "path": "main/02/001", "index": "1-1-1", "img": "../../../static/img/blood.png", "child": [] },
          { "title": "元模型", "path": "main/02/004", "index": "1-2-1", "img": "../../../static/img/blood.png", "child": [] },
        ]
      },

      {
        "title": "元数据统计分析管理", "path": "main/01", "index": "1-4", "img": "../../../static/img/manager.png", "child": [
          { "title": "元数据变更管理", "path": "main/01/001", "index": "1-4-1", "img": "../../../static/img/blood.png", "child": [] },
          { "title": "数据地图", "path": "main/01/002", "index": "1-4-2", "img": "../../../static/img/blood.png", "child": [] },
          {
            "title": "元数据分析", "path": "main/01/003", "index": "1-4-3", "img": "../../../static/img/yuanfenxi.png", "child": [

              { "title": "血缘分析", "path": "main/01/003/0001", "index": "1-4-3-1", "img": "../../../static/img/blood.png", "child": [] },
              { "title": "属性差异分析", "path": "main/01/003/0003", "index": "1-4-3-2", "img": "../../../static/img/chayi.png", "child": [] },
              { "title": "影响分析", "path": "main/01/003/0004", "index": "1-4-3-3", "img": "../../../static/img/impact.png", "child": [] },
            ]
          },

        ]
      },
    ]
  },
    {
      title: "规则管理",
      img: "../../../static/img/manager.png",
      index: '2',
      child: [
        { "title": "数据接口定义管理", "index": "2-1", "path": "main/03/001", "img": "../../../static/img/source.png", "child": [] },
        { "title": "数据转换规则管理", "index": "2-2", "path": "main/03/004", "img": "../../../static/img/modify.png", "child": [] },
      ]
    }
  ]
  return data
}
