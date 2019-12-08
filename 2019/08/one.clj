(require '[clojure.string :as str])

(def width 25)
(def height 6)
(def pixels_per_layer (* width height))




;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (str/trim (slurp f))))
    ;(reduce conj () (line-seq f))))
(println "Read" (count input) "puzzle characters from one line.")


