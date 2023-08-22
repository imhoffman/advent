
(require '[clojure.string :as str])
;(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(Integer/parseInt %) ,,)))

;;  for now, the key and :orig are redundant, until I figure out how I'll do this
(def d
  (reduce (fn [a b]
            (assoc a (first b) {:value (second b), :orig (first b), :curr (first b)}))
          {}
          (map vector (range (count input)) input)))

(prn input)
(prn d)






