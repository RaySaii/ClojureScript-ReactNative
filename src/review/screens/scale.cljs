(ns review.screens.scale
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [reagent.core :as r :refer [atom]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [chan put! <!]]
    [review.utils :refer [my-get]]
    [review.ui :refer [button flat-list image icon layout-animation header hair-line-width react-native screen-width screen-height touchable-opacity touchable-without-feedback text view]]
    ))


(def root-siblings (.-default (js/require "@bam.tech/react-native-root-siblings")))


(defn fetch-data [url params state]
      (js/console.log "fetch-data" url)
      (go (let [response (<! (http/get url))]
               (let [body (:body response)]
                    (js/console.log "response" (clj->js body))
                    (let [images (my-get body [:books] "无数据")]
                         (reset! state images)
                         ))
               )))

(defn active-item [url position]
      (let [start (r/atom false)
            show (r/atom false)
            unmount (r/atom false)]
           (r/create-class {:component-did-mount (fn []
                                                     (js/setTimeout #(do (.linear layout-animation)
                                                                         (reset! start true)) 200)
                                                     )
                            :reagent-render      (fn []
                                                     (when (not @unmount)
                                                           [view {:style {:width            screen-width
                                                                          :height           screen-height
                                                                          :position         "absolute"
                                                                          :zIndex           99
                                                                          :top              0
                                                                          :left             0
                                                                          :background-color "black"}}
                                                            [view {:style (merge (if @start
                                                                                   {:width  screen-width
                                                                                    :height screen-height
                                                                                    :top    0
                                                                                    :left   0}
                                                                                   {:width  97
                                                                                    :height 130
                                                                                    :top    (:y position)
                                                                                    :left   (:x position)})
                                                                                 {:position        "absolute"
                                                                                  :justify-content "center"
                                                                                  :align-items     "center"
                                                                                  }
                                                                                 )
                                                                   }
                                                             (when @show
                                                                   [touchable-opacity {:style    {:position "absolute"
                                                                                                  :top      15
                                                                                                  :left     15}
                                                                                       :on-press #(reset! unmount true)}
                                                                    [icon {:name  "times-circle"
                                                                           :size  27
                                                                           :color "white"}]
                                                                    ]
                                                                   )
                                                             [touchable-without-feedback {:on-press #(reset! show (not @show))}

                                                              [image {:style  (if @start
                                                                                {:width  "80%"
                                                                                 :height "60%"}
                                                                                {:width  "100%"
                                                                                 :height "100%"}
                                                                                )
                                                                      :source {:uri url}}]
                                                              ]
                                                             ]
                                                            ]

                                                           )
                                                     )}))
      )

(defn item-image [url]
      (let [!ref (r/atom nil)]
           (fn []
               [view {:style {:width           97
                              :height          130
                              :margin 10
                              :justify-content "center"
                              :align-items     "center"}

                      :ref   #(reset! !ref %)
                      }
                [touchable-without-feedback {:on-press (fn []
                                                           (do (.linear layout-animation)
                                                               (.measure @!ref (fn [ax, ay, width, height, px, py]
                                                                                   (root-siblings. (r/as-element [active-item url {:x px :y py}]))))
                                                               )
                                                           )
                                             }
                 [image {:style  {:width  "100%"
                                  :height "100%"}
                         :source {:uri url}}]
                 ]
                ]
               )
           )
      )


(defn scale [props]
      (let [images (r/atom [])]
           (fetch-data "https://api.douban.com/v2/book/search?q=clojure" {} images)
           (fn []
               [view {:style {:flex 1}}
                [header props "平滑放大"]
                [view {:style {:flex-direction "row"
                               :flex-wrap      "wrap"
                               :justify-content "center"}}
                 (doall (for [item @images]
                             ^{:key (:id item)}
                             [item-image (my-get item [:images :large] "review")]
                             )
                        )
                 ]

                ]
               )

           )
      )


(def path [:key :keys :keys])

