(ns review.navigator
  (:require [reagent.core :as r]
            [review.screens.index :refer [index]]
            [review.screens.swipe-delete :refer [swipe-delete]]
            [review.screens.drop-down :refer [drop-down]]
            [review.screens.cool-down :refer [cool-down]]
            [review.screens.scale :refer [scale]]
            [review.screens.data-list :refer [data-list]]
            [review.screens.chart :refer [chart]]
            [review.screens.map :refer [gaode-map]]
            [review.screens.upload-image :refer [upload-image]]
            ))

(def ReactNavigation (js/require "react-navigation"))

(def StackNavigator (.-StackNavigator ReactNavigation))

(def routes (clj->js {:Index       {:screen            (r/reactify-component index)
                                    :navigationOptions {:title "主页"}}
                      :SwipeDelete {:screen            (r/reactify-component swipe-delete)
                                    :navigationOptions {:title "滑动删除"}}
                      :DropDown    {:screen            (r/reactify-component drop-down)
                                    :navigationOptions {:title "下拉菜单"}}
                      :CoolDown    {:screen            (r/reactify-component cool-down)
                                    :navigationOptions {:title "定时器"}}
                      :Scale       {:screen            (r/reactify-component scale)
                                    :navigationOptions {:header nil}}
                      :DataList    {:screen            (r/reactify-component data-list)
                                    :navigationOptions {:title "数据长列表"}}
                      :Chart       {:screen            (r/reactify-component chart)
                                    :navigationOptions {:title "echarts"}}
                      :Map         {:screen            (r/reactify-component gaode-map)
                                    :navigationOptions {:title "地图"}}
                      :Upload        {:screen            (r/reactify-component upload-image)
                                    :navigationOptions {:title " 上传图片"}}
                      }))

(def stack-navigator (StackNavigator routes (clj->js {:initialRouteName "Index"})))

(defn app-navigator []
      [(r/adapt-react-class stack-navigator)])
