
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(partition (/ (count %) 2) %) ,,)
                ;(map (fn [s] (map #(apply str %) s)) ,,)
                (map (fn [s] (map #(frequencies %) s)) ,,)
                (map (fn [s] (map #(set (keys %)) s)) ,,)))

;(println input)


(defn priority [c]
  (cond
    (re-matches #"[A-Z]" (str c))
    (- (int c) 38)

    (re-matches #"[a-z]" (str c))
    (- (int c) 96)

    :else
    0))



(println
  (reduce (fn [total pair]
            (+ total
               (priority (first (set/intersection (first pair) (second pair))))))
          0 input))





