(ns review.screens.index
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [review.events]
            [review.ui :refer [map-view react-native view touchable-opacity text]]
            [review.subs]))


(def logo-img (js/require "./images/cljs.png"))


(def button-color "#0062DD")
(def button-width "80%")



(defn button [style on-press button-text]
      [touchable-opacity {:style    (merge {:padding       10
                                            :border-radius 5} style)
                          :on-press #(on-press)}
       [text {:style {:color       "white"
                      :text-align  "center"
                      :font-weight "bold"
                      }} (if (string? button-text)
                           button-text
                           @button-text)]]
      )

(defn index [props]
      (let [greeting (subscribe [:get-greeting])
            {:keys [navigation]} props
            navigate (aget navigation "navigate")]
           [view {:style {:margin      40
                          :align-items "center"}}
            [button {:background-color button-color, :width button-width} #(navigate "SwipeDelete") "滑动删除"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "DropDown") "下拉菜单"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "CoolDown") "定时器"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "Scale") "平滑放大"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "DataList") "数据长列表"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "Chart") "echarts"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "Map") "地图"]
            [button {:background-color button-color, :width button-width, :margin-top 10} #(navigate "Upload") " 上传图片"]
            ]))
