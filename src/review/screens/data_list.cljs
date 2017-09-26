(ns review.screens.data-list
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [reagent.core :as r :refer [atom]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [chan put! <!]]
    [review.utils :refer [my-get uid]]
    [review.ui :refer [button flat-list fast-image image icon activity-indicator layout-animation header hair-line-width refresh-control react-native screen-width screen-height touchable-opacity touchable-without-feedback text view]]
    ))



(defn fetch-data [start data]
      (js/console.log "fetch-data" (str "https://api.douban.com/v2/movie/top250?count=10&start=" start))
      (go (let [response (<! (http/get (str "https://api.douban.com/v2/movie/top250?start=" start)))]
               (let [body (:body response)]
                    (js/console.log "response" (clj->js body))
                    (let [movies (my-get body [:subjects] "无数据")]
                         (if (zero? start)
                           (reset! data movies)
                           (swap! data concat movies))
                         )
                    ))
          ))

(defn item [data]
      (let [i-data (.-item data)]
           (r/create-class {:component-will-mount    #(js/console.log "mount")
                            :should-component-update #(do (js/console.log "should")
                                                          false)
                            :component-did-update    #(js/console.log "did-update")
                            :component-did-mount     #(js/console.log "did-mount")
                            :component-will-update   #(js/console.log "will-update")
                            :reagent-render          (fn []
                                                         [view {:style {:width            screen-width
                                                                        :height           190
                                                                        :padding-left     15
                                                                        :paddingRight     15
                                                                        :flex-direction   "row"
                                                                        :align-items      "center"
                                                                        :background-color "white"}}
                                                          [view {:style {:width  110
                                                                         :height 150}}
                                                           [fast-image {:source {:uri (-> i-data .-images .-small)}
                                                                        :style  {:width  "100%"
                                                                                 :height "100%"}}]
                                                           ]
                                                          [view {:style {:height      "100%"
                                                                         :padding     20
                                                                         :padding-top 30}}
                                                           [text {:style {:font-size   16
                                                                          :font-weight "bold"
                                                                          }} (.-title i-data)]
                                                           [text {:style {:font-size  12
                                                                          :margin-top 10}} "导演:" (.-name (get (.-directors i-data) 0))]
                                                           [text {:style           {:font-size  12
                                                                                    :margin-top 4
                                                                                    :flex-wrap  "nowrap"}
                                                                  :number-of-lines 1} "主演:" (apply str (map #(str (.-name %) "  ") (.-casts i-data)))]
                                                           [text {:style {:font-size  14
                                                                          :margin-top 60}} "评分:" (-> i-data .-rating .-average)]
                                                           ]
                                                          ]
                                                         )
                            }
                           )))


(defn data-list []
      (let [start (atom 0)
            data (atom [])
            is-refreshing (atom false)]
           (fetch-data 0 data)
           (fn []
               [view {:style {:flex 1}}
                [flat-list {:data                   @data
                            :ItemSeparatorComponent #(r/as-element [view {:style {:height           hair-line-width
                                                                                  :background-color "#d8d8d8"
                                                                                  :width            screen-width}}])
                            :onEndReachedThreshold  0.5
                            :keyExtractor           #(uid)
                            :render-item            #(r/as-element [item %])
                            :on-end-reached         #(when (>= (count @data) 20)
                                                           (do (reset! start (+ @start 20))
                                                               (fetch-data @start data)
                                                               )
                                                           )
                            :ListFooterComponent    #(r/as-element
                                                       [view {:style {:justify-content "center"
                                                                      :align-items "center"
                                                                      :flex-direction "row"
                                                                      :margin-top 17}}
                                                        [activity-indicator]
                                                        [text {:style {:margin-left 7}} "正在加载"]
                                                        ]
                                                       )
                            :refreshing             @is-refreshing
                            :on-refresh             #(do (reset! is-refreshing true)
                                                         (reset! start 0)
                                                         (fetch-data 0 data)
                                                         (reset! is-refreshing false)
                                                         )
                            }]
                ]))

      )

