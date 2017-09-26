(ns review.screens.cool-down
  (:require
    [cljsjs.moment]
    [reagent.core :as r :refer [atom]]
    [review.ui :refer [react-native view icon button touchable-opacity touchable-without-feedback text screen-width hair-line-width layout-animation]]
    ))



(defn cool-down []
      (let [seconds-left (r/atom 5)
            show-time (r/atom false)
            timer (r/atom nil)
            start! (fn []
                       (reset! show-time true)
                       (reset! timer (js/setInterval #(reset! seconds-left (dec @seconds-left)) 1000)))]
           (r/create-class {:component-will-unmount #(js/clearInterval @timer)
                            :component-did-update   #(when (zero? @seconds-left)
                                                           (do (reset! seconds-left 5)
                                                               (reset! show-time false)
                                                               (js/clearInterval @timer)
                                                               (reset! timer nil)))
                            :reagent-render         (fn []
                                                        [view {:style {:flex        1
                                                                       :align-items "center"
                                                                       :margin-top  137}}
                                                         [button {:width            100
                                                                  :height           45
                                                                  :justify-content  "center"
                                                                  :align-items      "center"
                                                                  :padding          0
                                                                  :background-color (if (not @show-time) "blue" "#59A1F6")} #(when (not @show-time) (start!)) (if (and (> @seconds-left -1) @show-time)
                                                                                                                                                                (str @seconds-left "秒后重试")
                                                                                                                                                                "获取验证码")]])})))

