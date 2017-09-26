(ns review.screens.swipe-delete
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [review.utils :refer [uid splice find-map-index]]
            [review.ui :refer [react-native animated-view view text-input icon touchable-opacity text pan-responder screen-width hair-line-width]]
            [review.events]
            [review.subs]))

(def animated (.-Animated react-native))

(defonce app-state (r/atom (sorted-map)))
(defonce counter (atom 0))

(defn add-contact!
      "新增联系"
      []
      (let [id (swap! counter inc)]
           (swap! app-state assoc id {:id id, :text text})))

(defn remove-contact!
      "删除联系"
      [id]
      (swap! app-state dissoc id))


(defn set-value!
      "输入联系"
      [id text]
      (swap! app-state assoc-in [id :text] text))

(defn show-delete
      "点击滑动"
      [pos can-pan]
      (reset! can-pan true)
      (.start (animated.timing pos #js {:toValue 1, :duration 200})))

(defn responder-move [e, ges, pos, can-pan]
      (js/console.log "手势移动")
      (let [dx (.-dx ges)]
           (when (pos? dx)
                 (if (>= dx 65)
                   (do (.flattenOffset pos)
                       (.setValue pos 0)
                       (reset! can-pan false))
                   (.setValue pos (/ dx -65))))))

(defn responder-release [e, ges, pos, can-pan]
      (js/console.log "手势放开")
      (let [dx (.-dx ges)
            init-pos #(do (.setValue pos 0)
                          (reset! can-pan false))]
           (.flattenOffset pos)
           (if (neg? dx)
             (.start (animated.timing pos #js {:toValue  (if (< (- dx) (/ 65 2))
                                                           0
                                                           1)
                                               :duration 300})
                     #(when (and (.-finished %) (zero? (.-_value pos)))
                            (init-pos)))
             (.start (animated.timing pos #js {:toValue  (if (> dx (/ 65 2))
                                                           0
                                                           1)
                                               :duration 300})
                     #(when (and (.-finished %) (zero? (.-_value pos)))
                            (init-pos))))))

(defn responder-grant [pos]
      (js/console.log "手势触发")
      (.setOffset pos (.-_value pos))
      (.setValue pos 0))

(defn i-text-input [id]
      (let [item-height 45
            icon-left 13
            input-left (+ icon-left 30)
            pos (animated.Value. 0)
            can-pan (r/atom false)
            pr (.create pan-responder #js {:onStartShouldSetPanResponder        #(do true),
                                           :onStartShouldSetPanResponderCapture #(do true),
                                           :onMoveShouldSetPanResponder         #(do true),
                                           :onMoveShouldSetPanResponderCapture  #(do true),
                                           :onPanResponderTerminationRequest    #(do true),
                                           :onShouldBlockNativeResponder        #(do true),
                                           :onPanResponderGrant                 #(responder-grant pos)
                                           :onPanResponderMove                  #(responder-move %1 %2 pos can-pan),
                                           :onPanResponderRelease               #(responder-release %1 %2 pos can-pan)})]
           (fn []
               [animated-view {:style {:height           item-height,
                                       :width            (+ screen-width 45)
                                       :flex-direction   "row"
                                       :align-items      "center"
                                       :transform        [{:translateX (.interpolate pos (clj->js {:inputRange [0 1], :outputRange [0 -45]}))}],
                                       :background-color "white"}}
                [view {:style {:width           45,
                               :height          item-height
                               :align-items     "center"
                               :justify-content "center"}}
                 [icon {:name     "minus-circle"
                        :size     20
                        :color    "red",
                        :on-press #(show-delete pos can-pan)}]]
                [view (merge (if @can-pan
                               (js->clj (.-panHandlers pr))
                               {})
                             {:style {:width               (- screen-width 45)
                                      :height              item-height
                                      :flex-direction      "row",
                                      :border-bottom-width hair-line-width
                                      :border-color        "#d8d8d8"
                                      :align-items         "center"}})
                 [text (str "联系方式" (inc (find-map-index (vals @app-state) id)))]
                 [text-input {:on-change-text    #(set-value! id %)
                              :clear-button-mode "while-editing"
                              :placeholder       "请输入联系方式"
                              :style             {:width        "80%"
                                                  :font-size    14
                                                  :height       item-height
                                                  :padding-left 10
                                                  :border-color "#d8d8d8"}}]]
                [touchable-opacity {:on-press #(remove-contact! id)
                                    :style    {:background-color "red",
                                               :width            45,
                                               :position         "absolute",
                                               :right            0,
                                               :height           item-height,
                                               :justify-content  "center",
                                               :align-items      "center"}}
                 [text {:style {:color "white"}} "删除"]]])))


(defn swipe-delete [props]
      (let [item-height 45
            icon-left 13
            input-left (+ icon-left 30)]
           (r/create-class {:component-will-unmount #(reset! app-state (sorted-map))
                            :reagent-render         (fn []
                                                        [view {:style {:flex 1}}
                                                         [view {:style {:margin-top 50}}
                                                          (for [item (vals @app-state)]
                                                               ^{:key (:id item)} [i-text-input (:id item)])
                                                          [touchable-opacity {:style    {:height item-height}
                                                                              :on-press #(add-contact!)}
                                                           [view
                                                            [view {:style {:position         "absolute"
                                                                           :zIndex           99
                                                                           :height           item-height,
                                                                           :justify-content  "center",
                                                                           :padding-left     icon-left,
                                                                           :background-color "transparent"}}
                                                             [icon {:name  "plus-circle"
                                                                    :size  20
                                                                    :color "#0062DD"}]]
                                                            [text {:style {:background-color "white"
                                                                           :height           item-height,
                                                                           :padding-top      (/ item-height 3),
                                                                           :padding          10,
                                                                           :padding-left     input-left,
                                                                           :border-radius    5}} "添加其他联系方式"]]]]])}

                           )))
