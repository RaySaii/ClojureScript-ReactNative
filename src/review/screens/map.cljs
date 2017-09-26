(ns review.screens.map
  (:require
    [reagent.core :as r :refer [atom]]
    [review.ui :refer [react-native marker map-view view icon button touchable-opacity touchable-without-feedback text screen-width screen-height hair-line-width layout-animation]]
    )
  )


(def coordinate {:latitude  30.182159
                 :longitude 120.139843})
(defn gaode-map []
      (fn []
          [view {:style {:flex 1}}
           [text "ok"]
           [map-view {:style      {:position "absolute"
                                   :width    screen-width
                                   :height   screen-height
                                   :top      0
                                   :left     0}
                      :coordinate coordinate
                      :zoom-level 15}
            [marker {:coordinate coordinate
                     :title "海创园"
                     :active true}]]
           ])
      )

