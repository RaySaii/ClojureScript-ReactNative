(ns review.screens.upload-image
  (:require
    [reagent.core :as r :refer [atom]]
    [review.ui :refer [view icon image button touchable-opacity touchable-without-feedback text screen-width screen-height hair-line-width]]
    )
  )

(def progress (r/adapt-react-class (.-Pie (js/require "react-native-progress"))))


(do (js/console.log progress))

(def image-picker (.-default (js/require "react-native-image-crop-picker")))

(defn confirm [counter images res]
      (js/console.log "path" (js->clj res))
      (doall (map #(when (< (count @images) 3)
                         (let [id  (swap! counter inc)]
                              (swap! images assoc id {:id id :path (aget % "path")}))
                         )
                  res))
      (js/console.log "res" @images)
      )


(defn progress-image [images id props]
      (let [prog (r/atom 0)
            timer (r/atom nil)
            delete (r/atom false)]
           (r/create-class {:component-did-mount    (fn []
                                                        (reset! timer (js/setInterval #(reset! prog (+ @prog (rand))) 500))
                                                        )
                            :component-did-update   #(when (= prog 1)
                                                           (do (reset! timer nil)
                                                               (js/clearInterval timer)))
                            :component-will-unmount #(js/clearInterval timer)
                            :reagent-render         (fn []
                                                        (when (not @delete)
                                                              [view {:style (:style props)}
                                                               [image {:source {:uri (get-in props [:source :uri])}
                                                                       :style  {:justify-content "center"
                                                                                :align-items     "center"
                                                                                :width           "100%"
                                                                                :height          "100%"
                                                                                }}
                                                                (when (<= @prog 1)
                                                                      [progress {:progress     @prog
                                                                                 :border-color "rgba(255,255,255,.7)"
                                                                                 :color        "rgba(255,255,255,.7)"}])
                                                                ]
                                                               [touchable-opacity {:style    {:width            20
                                                                                              :height           20
                                                                                              :position         "absolute"
                                                                                              :right            0
                                                                                              :top              0
                                                                                              :background-color "white"
                                                                                              :justify-content  "center"
                                                                                              :align-items      "center"
                                                                                              }
                                                                                   :on-press #(do (reset! delete true)
                                                                                                  (swap! images dissoc id))}
                                                                [icon {:name  "times"
                                                                       :size  20
                                                                       :color "red"
                                                                       }]
                                                                ]
                                                               ])
                                                        )})
           )
      )

(defn open-picker [counter images]
      (-> image-picker
          (.openPicker #js {"multiple" true "width" 100 "height" 100 "cropping" true "maxFiles" 3})
          (.then #(confirm counter images %))
          (.catch #(js/console.log "err" %))
          ))

(defn upload-image []
      (let [images (r/atom (sorted-map))
            counter (r/atom 0)]
           (fn []
               (js/console.log "images"  (clj->js  @images))
               [view {:style {:flex 1}}
                [view {:style {:width            screen-width
                               :height           110
                               :background-color "white"
                               :align-items      "center"
                               :padding-right    15
                               :flex-direction   "row"}}
                 (for [item (vals @images)]
                      ^{:key (:id item)} [progress-image images (:id item) {:source {:uri (:path item)}
                                                                            :style  {:width       80
                                                                                     :height      80
                                                                                     :margin-left 15
                                                                                     }}])
                 (when (< (count @images) 3)
                       [touchable-opacity {:style    {:width            80
                                                      :height           80
                                                      :background-color "#d8d8d8"
                                                      :justify-content  "center"
                                                      :align-items      "center"
                                                      :margin-left      15
                                                      }
                                           :on-press #(open-picker counter images)}
                        [text {:style {:font-size   30
                                       :line-height 30}} "+"]])
                 ]
                ])))
