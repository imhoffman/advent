
(require '[clojure.string :as str])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([ABC]) ([XYZ])" %) ,,)
                (map rest)
                (map vec)
                ;(map #(into {} (apply vector [:1 :2] %)) ,,)))
                (map #(assoc {} :1 (% 0), :2 (% 1)) ,,)))

;(println input)

(def d
  {"A" {"X" 3, "Y" 1, "Z" 2},
   "B" {"X" 1, "Y" 2, "Z" 3},
   "C" {"X" 2, "Y" 3, "Z" 1}})

  (println
    (reduce
      (fn [score round]
        (let [one (round :1)
              two (round :2)]
          (+ score
             (case two, "X" 0, "Y" 3, "Z" 6, 0)
             ((d one) two))))
      0 input))



