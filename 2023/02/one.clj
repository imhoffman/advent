
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"Game (\d+): (.*)" %) ,,)
                (reduce (fn [a b] (assoc a (Integer/parseInt (second b)) (nth b 2))) {} ,,)))

;(println input)

(defn legit-game? [s]
  (let [sets (#(str/split % #";") s)
        ds   (->> sets
                  (map #(re-seq #"(\d+) (red|blue|green)" %) ,,)
                  (map #(reduce (fn [a b] (assoc a (nth b 2) (Integer/parseInt (nth b 1)))) {} %) ,,))]
    (every? true?
           (for [d ds]
             (and (>= 12 (or (d "red") 0)) (>= 13 (or (d "green") 0)) (>= 14 (or (d "blue") 0)))))))


(println
  (reduce
    (fn [a b]
      (+ a (if (legit-game? (second b)) (first b) 0))) 0 input))


