
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #",") ,,)
       (map #(Integer/parseInt %) ,,)))


(let [xmin (apply min input)
      xmax (apply max input)]
  (loop [x xmin
         best (* (count input) xmax)]
    (if (= x xmax)
      (println best)
      (recur
        (inc x)
        (let [curr (apply + (for [n input] (Math/abs (- x n))))]
          (if (> best curr)
            curr
            best))))))

