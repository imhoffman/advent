(require '[clojure.string :as str])



;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))
(println "Read" (count input) "lines.")

