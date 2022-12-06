
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input (->> "puzzle.txt"
                slurp
                str/trim))

(loop [s (apply vector input)
       c 4]
  (if (= 4 (count (frequencies (take 4 s))))
    (println c)
    (recur
      (rest s)
      (inc c))))








