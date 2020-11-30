
(require '[clojure.string :as str])

(def input-as-vec-of-longs
  (vec (for [c (str/trim (slurp "puzzle.txt"))] (Long/parseLong (str c)))))

(println input-as-vec-of-longs)

