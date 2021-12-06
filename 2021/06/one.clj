
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #",") ,,)
       (map #(Integer/parseInt %) ,,)))



(loop [day 1
       timers input]
  (if (= day 81)
    (println (count timers))
    (recur
      (inc day)
      (reduce
        (fn [a b]
          (if (= b 0)
            (apply conj a [6 8])
            (conj a (dec b))))
        (list)
        timers))))



