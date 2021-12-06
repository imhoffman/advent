
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
  (if (= day 257)
    (println (count timers))
    (recur
      (inc day)
      (let [f (frequencies timers)
            times (keys f)]
        (reduce
          (fn [a t]
            (if (= t 0)
              (concat a (take (f t) (repeat 6)) (take (f t) (repeat 8)))
              (apply conj a (take (f t) (repeat (dec t))))))
          (list)
          times)))))



