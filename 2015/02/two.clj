
(require '[clojure.string :as str])

(def list-of-dimension-lists
  ((fn [s]
     (->> s
          slurp
          (re-seq #"\w+")
          (map #(str/split % #"[x]"))
          (map #(map (fn [r] (Long/parseLong r)) %))))
   "puzzle.txt"))

