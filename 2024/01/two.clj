
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                ;(#(str/split % #"\n") ,,)
                (re-seq #"(\d+)\s*?(\d+)\n" ,,)
                (map rest ,,)
                (reduce (fn [a b]
                            (vector
                            (conj (nth a 0) (Integer/parseInt (nth b 0)))
                            (conj (nth a 1) (Integer/parseInt (nth b 1)))))
                        [[][]] ,,)
                (map sort ,,)))

;(println input)

(println
  (let [appears (frequencies (nth input 1))]
    (apply + (for [n (nth input 0)]
               (* n (or (appears n) 0))))))


