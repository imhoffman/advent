
(require '[clojure.string :as str])

(def tree-map
  ((fn [s] (-> s
               slurp
               (str/split #"\s")
               vec))
   "puzzle.txt"))


;;  given
(def slopes
  [ [1 1], [3 1], [5 1], [7 1], [1 2] ] )


(defn product [input vec-of-slope-vecs]
  (let [v     (vec (map vec input))
        ncols (count (input 0))
        nrows (count input)]
    (loop [stack vec-of-slope-vecs
           accum 1]
      (if (empty? stack)
        accum
        (recur
          (rest stack)
          (* accum
            (loop [row   0
                   col   0
                   trees 0]
              (if (> (+ row ((first stack) 1)) nrows)
                (do
                  (println (first stack) "yields" trees "trees")
                  trees)
                (recur
                  (+ row ((first stack) 1))
                  (+ col ((first stack) 0))
                  (if (= ((v row) (mod col ncols)) \#)
                    (inc trees)
                    trees))))))))))

(println (product tree-map slopes))


;; 3435931200
;; 3413906000
;; 3350032920
;; 3521829480  <--  correct; final slope is off by one tree (?)

