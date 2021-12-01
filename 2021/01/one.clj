
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(Integer/parseInt %) ,,)))


(loop [in input
       c  0]
  (let [prev (first in)
        curr (second in)]
    (if (= 1 (count in))
      (println c)
      (recur
        (rest in)
        (if (> curr prev) (inc c) c)))))



