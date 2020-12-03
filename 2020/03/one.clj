
(require '[clojure.string :as str])

(def tree-map
  ((fn [s] (-> s
               slurp
               (str/split #"\s")
               vec))
   "puzzle.txt"))


(println
(let [v     (vec (map vec tree-map))
      ncols (count (tree-map 0))
      nrows (count tree-map)]
  (loop [row   0
         col   0
         trees 0]
    (if (= row nrows)
      trees
      (recur
        (+ row 1)
        (+ col 3)
        (if (= ((v row) (mod col ncols)) \#)
          (inc trees)
          trees)))))
)



