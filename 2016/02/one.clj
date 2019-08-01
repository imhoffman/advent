(require '[clojure.string :as str])

;;  https://clojuredocs.org/clojure.core/to-array-2d
(def pad (to-array-2d [ [1 2 3] [4 5 6] [7 8 9] ]))

;(println " this should be 6:" (aget pad 1 2))

(defn combo [s]
  (for [r s]
    (let [a (seq r)
          i 1       ; horizontal index on keypad
          j 1]      ; vertical index   start on "5" at 1,1
    (for [p a]
      (case p
        \U (println "read a U")
        \D (println "read a D")
        \L (println "read a L")
        \R (println "read a R"))))))

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def document
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))
(println " number of lines read from input file:" (count document))

(println (combo document))


