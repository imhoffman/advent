
(require '[clojure.string :as str])
;(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([a-z]{4}): ([a-z]{4}|\d+)\s?([\+\-\*\/])?\s?([a-z]{4}|\d+)?" %) ,,)
                (map rest)
                (map (fn [ss]
                       (reduce (fn [a b] (conj a (if (and b (re-matches #"\d+" b)) (Integer/parseInt b) b)))
                               [] ss)) ,,)
                (reduce #(assoc %1 (first %2) (rest %2)) {} ,,)))

;(prn input)

(println
  ((resolve (read-string "+")) 3 5 7))








