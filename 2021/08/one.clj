
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #".*\| (\w+) (\w+) (\w+) (\w+).*" %) ,,)
       (map rest)))

(println
  (reduce
    (fn [a b]
      (+ a (count (filter #(or (= 2 (count %)) (= 4 (count %)) (= 3 (count %)) (= 7 (count %))) b))))
    0 input))







