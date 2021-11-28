
(require '[clojure.string as :str])

(def polymer (->> "puzzle.txt"
                  slurp))

(reduce (fn [a b]
          (cond
            (empty? b)
            (println (count a))

            (or
              (re-matches #"([a-z])([A-Z])" (apply str (last a) (first b)))
              (re-matches #"([A-Z])([a-z])" (apply str (last a) (first b))))
            (conj (vec a) ... xor upper/lower)))

            :else
            (conj a (first b)))




