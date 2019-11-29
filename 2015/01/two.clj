(require '[clojure.string :as str])

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (first (reduce conj () (line-seq f)))))      ; for a single-line input

((fn [directions index floor]
   (if (= floor -1) (println "\n basement encountered at floor" (dec index)))
   (if (= (first directions) \()
     (recur (rest directions) (inc index) (inc floor))
     (recur (rest directions) (inc index) (dec floor)))
   ) input 1 0)

