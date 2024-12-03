
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (re-seq #"mul\((\d+),(\d+)\)" ,,)
                (map rest ,,)
                (map (fn [a] (map #(Integer/parseInt %) a)) ,,)))

(println input)

(println (reduce (fn [a b] (+ a (* (first b) (second b)))) 0 input))


