
(require '[clojure.string :as str])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([ABC]) ([XYZ])" %) ,,)
                (map rest)
                (map vec)
                ;(map #(into {} (apply vector [:1 :2] %)) ,,)))
                (map #(assoc {} :1 (% 0), :2 (% 1)) ,,)))

(println input)

(def d
  {"A" {"Z" :win, "Y" :lose, "X" :draw},
   "B" {"X" :win, "Z" :lose, "Y" :draw},
   "C" {"Y" :win, "X" :lose, "Z" :draw}})

  (println
    (reduce
      (fn [score round]
        (let [one (round :1)
              two (round :2)
              outcome ((d one) two)]
          (println " score:" score "processing" round "with outcome" outcome)
          (+ score
             (case two, "X" 1, "Y" 2, "Z" 3, 0)
             (case outcome, :win 6, :draw 3, :lose 0, 0))))
      0 input))


;;  7722 too low
;;  13712 too high

