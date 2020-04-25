
(require '[clojure.string :as str])

(defn differencer [s]
  (- (count s)
     ((frequencies s) \newline)
     (loop [stack s
            accum 0]
       (if (empty? stack)
         accum
         (case (first stack)
           \" (recur (rest stack) (+ accum 1))
           \\ (case (second stack)
                \" (recur (rest (rest stack)) (+ accum 1))
                \\ (recur (rest (rest stack)) (+ accum 1))
                \x (recur (rest (rest stack)) (+ accum 3)))
           (recur (rest stack) accum))))))


(def input-list-of-data-entries
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))

(println " read" (count input-list-of-data-entries) "lines")


;; 4978 is too high
;; 4977 is too high

