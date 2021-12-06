
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #",") ,,)
       (map #(Integer/parseInt %) ,,)))



(loop [day 1
       fish (loop [i 0
                   o (frequencies input)]
              (if (= i 9)
                o
                (recur
                  (inc i)
                  (if (contains? o i) o (assoc o i 0)))))]
  (if (= day 257)
    (println (apply + (vals fish)))
    (recur
      (inc day)
      (loop [t 8
             d fish]
        (if (= t 0)
          (assoc d 8 (fish 0), 6 (+ (fish 7) (fish 0)))
          (recur
            (dec t)
            (assoc d (dec t) (fish t))))))))





