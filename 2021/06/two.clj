
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #",") ,,)
       (map #(Integer/parseInt %) ,,)))



(let [f (frequencies input)
      ts (keys f)]
  (println
    (apply +
           (for [t ts] (* (f t) (quot 256 t)))
           ;; some other term with the additional 8s
           )))


