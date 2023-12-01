
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def d {"zero" "0",
        "one" "1",
        "two" "2",
        "three" "3",
        "four" "4",
        "five" "5",
        "six" "6",
        "seven" "7",
        "eight" "8",
        "nine" "9"})

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-seq #"\d|zero|one|two|three|four|five|six|seven|eight|nine" %) ,,)
                (map #(reduce (fn [a b] (conj a (if (d b) (d b) b))) [] %) ,,)
                (map #(str (first %) (last %)) ,,)
                (map #(Integer/parseInt %) ,,)))

;(println input)

(println (apply + input))




