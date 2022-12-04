
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(frequencies %) ,,)
                (map #(set (keys %)) ,,)))

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
  (reduce (fn [total group-pairs]
            (prn (flatten group-pairs))
            (+ total
               (priority (first (apply set/intersection (flatten group-pairs))))))
          0 (partition 3 input)))





