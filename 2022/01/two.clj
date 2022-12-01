
(require '[clojure.string :as str])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)
                (map #(str/split % #"\n") ,,)
                (map #(map (fn [cals] (Integer/parseInt cals)) %) ,,)))

(println (apply + (take 3 (sort > (map #(apply + %) input)))))


