
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
      (if (and (not= (b 0) (b 2)) (not= (b 1) (b 3)))
        a
        (let [pairs (if (= (b 1) (b 3))
                      (let [[xmin xmax] (if (> (b 0) (b 2)) [(b 2) (inc (b 0))] [(b 0) (inc (b 2))])]
                        (for [x (range xmin xmax)] (vector x (b 1))))
                      (let [[ymin ymax] (if (> (b 1) (b 3)) [(b 3) (inc (b 1))] [(b 1) (inc (b 3))])]
                        (for [y (range ymin ymax)] (vector (b 0) y))))]
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
                    1))))))))
    {}
    input))


(println (count (filter #(< 1 %) (vals points))))



