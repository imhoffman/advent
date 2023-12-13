
(require '[clojure.string :as str])
;(require '[clojure.set :as set])
;(require '[clojure.core.reducers :as r])
;(require '[clojure.math.combinatorics :as combo])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)
                (mapv #(str/split % #"\n") ,,)
                (partition 2 2 ,,)))


(doseq [pair input]
  (doseq [e pair]
    (doseq [line e]
      (prn line))))




