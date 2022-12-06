
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input (->> "puzzle.txt"
                slurp
                str/trim))

(loop [s (apply vector input)
       c 14]
  (if (= 14 (count (frequencies (take 14 s))))
    (println c)
    (recur
      (rest s)
      (inc c))))








