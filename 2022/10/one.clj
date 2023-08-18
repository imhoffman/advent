
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"(addx|noop)\s?(\-?\d+)?" %) ,,)
                (map #(vec (rest %)) ,,)
                (map #(if (% 1) (vector (% 0) (Integer/parseInt (% 1))) %) ,,)))

(prn input)





