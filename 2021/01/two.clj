
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(Integer/parseInt %) ,,)))


(loop [in input
       c  0]
  (let [prev (apply + (take 3 in))
        curr (apply + (rest (take 4 in)))]
    (if (= 3 (count in))
      (println c)
      (recur
        (rest in)
        (if (> curr prev) (inc c) c)))))



