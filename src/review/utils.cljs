(ns review.utils)

(defn uid []
      (js* "Math.random().toString(32).slice(2)"))

(defn splice [v index]
      (vec (concat (subvec v 0 index)
                   (subvec v (inc index)))))

(defn map-indices [f coll]
      (keep-indexed #(when (f %2) %1) coll))
(defn find-map-index [m id]
      (js/console.log "目标 map" (clj->js m) "目标 id" id)
      (first (map-indices #(= (:id %) id) m)))

(defn my-get [target path default]
      (if (seq (splice path 0))
        (if-let [target (get target (first path))]
                (recur target (splice path 0) default)
                default)
        (get target (first path) default)))
