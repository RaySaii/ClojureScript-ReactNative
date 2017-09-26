(ns review.screens.drop-down
  (:require
    [reagent.core :as r :refer [atom]]
    [review.ui :refer [react-native view icon touchable-opacity touchable-without-feedback text screen-width hair-line-width layout-animation]]
    ))

(defn toggle-drop [show]
      (.easeInEaseOut layout-animation)
      (reset! show (not @show)))

(defn drop-down []
      (let [show (r/atom false)
            selected-title (r/atom "")
            button-style {:flex            1
                          :height          "100%"
                          :justify-content "center"
                          :align-items     "center"}
            item-style {:width               "100%"
                        :height              45
                        :border-bottom-width hair-line-width
                        :border-color        "#d8d8d8"
                        :padding-left        28
                        :justify-content     "center"}]
           (fn []
               [view {:style {:flex 1}}
                (if @show
                  [touchable-without-feedback {:on-press #(toggle-drop show)}
                   [view {:style {:width            screen-width
                                  :height           "100%"
                                  :position         "absolute"
                                  :top              0
                                  :left             0
                                  :background-color "rgba(0,0,0,.3)"}}]]
                  nil)
                [view {:style {:width            "100%"
                               :height           (if @show
                                                   nil
                                                   0)
                               :overflow         "hidden"
                               :background-color "white"
                               :position         "absolute"
                               :top              40}}
                 [touchable-opacity {:style    item-style
                                     :on-press (fn []
                                                   (toggle-drop show)
                                                   (reset! selected-title ""))}
                  [text "默认排序"]]
                 (for [item (range 5)]
                      ^{:key item} [touchable-opacity {:style    item-style
                                                       :on-press (fn []
                                                                     (toggle-drop show)
                                                                     (reset! selected-title (str "排序" (inc item))))}
                                    [text (str "排序" (inc item))]])]
                [view {:style {:width               screen-width
                               :height              40
                               :background-color    "white"
                               :border-bottom-width hair-line-width
                               :border-color        "#d8d8d8"
                               :flex-direction      "row"
                               :align-items         "center"}}
                 [touchable-opacity {:style    (merge button-style {:flex-direction "row"})
                                     :on-press #(toggle-drop show)}
                  [text (if (clojure.string/blank? @selected-title)
                          "排序"
                          @selected-title)]
                  (if @show
                    [icon {:name  "chevron-up"
                           :size  10
                           :color "black"}]
                    [icon {:name  "chevron-down"
                           :size  10
                           :color "black"}])
                  ]
                 [touchable-opacity {:style button-style}
                  [text "other1"]]
                 [touchable-opacity {:style button-style}
                  [text "other2"]]
                 [touchable-opacity {:style button-style}
                  [text "other3"]]
                 ]])))