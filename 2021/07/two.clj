
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #",") ,,)
       (map #(Integer/parseInt %) ,,)))


(defn sumtorial [n a]
  (if (= n 0)
    a
    (recur (dec n) (+ n a))))


(let [xmin (apply min input)
      xmax (apply max input)]
  (loop [x xmin
         best (* (count input) xmax xmax)]
    (if (= x xmax)
      (println best)
      (recur
        (inc x)
        (let [curr (apply + (for [n input] (sumtorial (Math/abs (- x n)) 0)))]
          (if (> best curr)
            curr
            best))))))

