(require '[clojure.string :as str])


;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (first (reduce conj () (line-seq f)))))      ; for a single-line input

;(println (count input))                          ; the number of characters

(let [freqs (frequencies input)]
  (println " final floor:" (- (get freqs \() (get freqs \)))))

