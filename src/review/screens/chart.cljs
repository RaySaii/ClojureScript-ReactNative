(ns review.screens.chart
  (:require
    [cljsjs.moment]
    [reagent.core :as r :refer [atom]]
    [review.ui :refer [react-native echarts view icon button touchable-opacity touchable-without-feedback text screen-width screen-height hair-line-width layout-animation]]
    )
  )


(def option {
             ;:title   {:text "堆叠区域图"}
             :tooltip {:trigger     "axis"
                       :axisPointer {:type  "cross"
                                     :label {:background-color "#6a7985"}}
                       }
             :legend  {:data ["邮件营销", "联盟广告", "视频广告" "直接访问" "搜索引擎"]}
             :grid    {:left         "3%"
                       :right        "4%"
                       :bottom       "3%"
                       :containLabel true}
             :xAxis   [{:type        "category"
                        :boundaryGap false
                        :data        ["周一" "周二" "周三" "周四" "周五" "周六" "周日"]
                        }]
             :yAxis   [{:type "value"}]
             :series  [{:name      "邮件营销"
                        :type      "line"
                        :stack     "总量"
                        :areaStyle {:normal {}}
                        :data      [120, 132, 101, 134, 90, 230, 210]
                        }
                       {:name      "联盟广告"
                        :type      "line"
                        :stack     "总量"
                        :areaStyle {:normal {}}
                        :data      [220, 182, 191, 234, 290, 330, 310]
                        }
                       {:name      "视频广告"
                        :type      "line"
                        :stack     "总量"
                        :areaStyle {:normal {}}
                        :data      [150, 232, 201, 154, 190, 330, 410]
                        }
                       {:name      "直接访问"
                        :type      "line"
                        :stack     "总量"
                        :areaStyle {:normal {}}
                        :data      [320, 332, 301, 334, 390, 330, 320]
                        }
                       {:name      "搜索引擎"
                        :type      "line"
                        :stack     "总量"
                        :label     {:normal {:show     true
                                             :position "top"}}
                        :areaStyle {:normal {}}
                        :data      [820, 932, 901, 934, 1290, 1330, 1320]
                        }
                       ]
             })

(defn chart []
      [view {:style {:flex       1
                     :margin-top 57}}
       [echarts {:option option}]
       ]
      )
