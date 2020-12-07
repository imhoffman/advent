
(require '[clojure.string :as str])

(prn
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(Long/parseLong %))
       (apply +)))

