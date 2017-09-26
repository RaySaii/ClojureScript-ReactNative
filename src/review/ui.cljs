(ns review.ui
  (:require [reagent.core :as r])
  )

(def react-native (js/require "react-native"))

(def navigation)

(def Icon (js/require "react-native-vector-icons/FontAwesome"))

(def fast-image (r/adapt-react-class (.-default (js/require "react-native-fast-image"))))

(def Amap (js/require "react-native-amap3d"))
(def map-view (r/adapt-react-class (.-MapView Amap)))
(def marker (r/adapt-react-class (.-Marker Amap)))

(def echarts (r/adapt-react-class (.-default (js/require "echarts"))))


(def icon (r/adapt-react-class (.-default Icon)))
(def text (r/adapt-react-class (.-Text react-native)))
(def view (r/adapt-react-class (.-View react-native)))

(def web-view (r/adapt-react-class (.-WebView react-native)))
(def image (r/adapt-react-class (.-Image react-native)))
(def animated (.-Animated react-native))
(def layout-animation (.-LayoutAnimation react-native))
(def animated-view (r/adapt-react-class (.-View animated)))
(def touchable-opacity (r/adapt-react-class (.-TouchableOpacity react-native)))
(def text-input (r/adapt-react-class (.-TextInput react-native)))
(def flat-list (r/adapt-react-class (.-FlatList react-native)))
(def activity-indicator (r/adapt-react-class (.-ActivityIndicator react-native)))
(def StyleSheet (.-StyleSheet react-native))
(def Dimensions (.-Dimensions react-native))
(def pan-responder (.-PanResponder react-native))
(def refresh-control (r/adapt-react-class (.-RefreshControl react-native)))
(def screen-width (.-width (.get Dimensions "window")))
(def screen-height (.-height (.get Dimensions "window")))
(def hair-line-width (.-hairlineWidth StyleSheet))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight react-native)))
(def touchable-without-feedback (r/adapt-react-class (.-TouchableWithoutFeedback react-native)))

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


(defn header [props title]
      (js/console.log "header")
      (let [goBack (aget (:navigation props) "goBack")]
           [view {:style {:width            screen-width
                          :padding-top      20
                          :height           65
                          :background-color "white"}}
            [view {:style {:width               screen-width
                           :height              45
                           :flex-direction      "row"
                           :align-items         "center"
                           :border-bottom-width hair-line-width
                           :border-color        "#d8d8d8"}}
             [view {:style {:flex 1}}
              [touchable-opacity {:on-press #(goBack)
                                  :style    {:width           30
                                             :height          "100%"
                                             :padding-left    15
                                             :justify-content "center"}}
               [icon {:name  "angle-left"
                      :color "black"
                      :size  30}]
               ]
              ]
             [view {:style {:flex 1, :align-items "center"}}
              [text {:style {:font-size   17
                             :font-weight "bold"}} title]]
             [view {:style {:flex 1}}]
             ]
            ]
           )
      )
