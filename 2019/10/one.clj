(require '[clojure.string :as str])


;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
    ;(reduce conj [] (line-seq f))))
(println "Read" (count input) "lines.")

(def width (count (get input 0)))
(def height (count input))

(doseq [i (range height)]
  (println (get input i)))

