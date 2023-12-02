
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "input.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"Game (\d+): (.*)" %) ,,)
                (reduce (fn [a b] (assoc a (Integer/parseInt (second b)) (nth b 2))) {} ,,)))

;(println input)

(defn game-power [s]
  (let [sets (#(str/split % #";") s)
        ds   (->> sets
                  (map #(re-seq #"(\d+) (red|blue|green)" %) ,,)
                  (map #(reduce (fn [a b] (assoc a (nth b 2) (Integer/parseInt (nth b 1)))) {} %) ,,))
        cd   (reduce (fn [a b]
                       (loop [o a, d b]
                         (if (empty? d)
                           o
                           (let [[k,v] (first d)]
                             (recur (assoc o k (conj (o k) v)) (vec (rest d)))))))
                     {"red" [], "green" [], "blue" []}
                     ds)
        maxd (reduce (fn [a b] (assoc a (first b) (apply max (second b)))) {} cd)]
    (apply * (vals maxd))))


(println
  (apply + (for [game input] (game-power (second game)))))


