
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-seq #"(e|se|sw|w|nw|ne)+?" %) ,,)
       (map #(map last %) ,,)))

(prn input)

