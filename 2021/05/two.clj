
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"(\d+),(\d+) -> (\d+),(\d+)" %) ,,)
       (map rest ,,)
       (map (fn [e] (mapv #(Integer/parseInt %) e)) ,,)))



(def points
  (reduce
    (fn [a b]
      (let [pairs
            (let [[xmin xmax] (if (> (b 0) (b 2)) [(b 2) (inc (b 0))] [(b 0) (inc (b 2))])
                  [ymin ymax] (if (> (b 1) (b 3)) [(b 3) (inc (b 1))] [(b 1) (inc (b 3))])]
              (cond
                (= xmin (dec xmax))
                  (for [y (range ymin ymax)] (vector xmin y))
                (= ymin (dec ymax))
                  (for [x (range xmin xmax)] (vector x ymin))
                :else
                  (map vector
                       (if (> (b 0) (b 2)) (range (b 0) (dec (b 2)) -1) (range (b 0) (inc (b 2))))
                       (if (> (b 1) (b 3)) (range (b 1) (dec (b 3)) -1) (range (b 1) (inc (b 3)))))))]
        (loop [ps pairs
               out a]
          (if (empty? ps)
            out
            (recur
              (rest ps)
              (assoc
                out
                (first ps)
                (if (contains? out (first ps))
                  (inc (out (first ps)))
                  1)))))))
    {}
    input))


;(doseq [y (range 10)]
;  (doseq [x (range 10)] (print (if (contains? points (vector x y)) (points (vector x y)) ".")))
;  (println))

(println (count (filter #(< 1 %) (vals points))))



