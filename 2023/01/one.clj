
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map (fn [s] (filter #(re-matches #"\d" (str %)) (vec s))) ,,)
                (map #(str (first %) (last %)) ,,)
                (map #(Integer/parseInt %) ,,)))

;(println input)

(println (apply + input))




