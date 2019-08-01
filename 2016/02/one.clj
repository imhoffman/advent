(require '[clojure.string :as str])

;;  https://clojuredocs.org/clojure.core/to-array-2d
(def pad (to-array-2d [ [1 2 3] [4 5 6] [7 8 9] ]))

;(println " this should be 6:" (aget pad 1 2))

(defn combo [s]
  (let [mi (- (alength (aget pad 0)) 1)     ; max index dimensions on keypad
        mj (- (alength pad) 1)]
  (loop [r s        ; loop through lines of instructions
         fi 1       ; "vertical" index on keypad
         fj 1]      ; "horizontal" index   start on "5" at 1,1
    (let [b (first r)
          c (rest r)]
    ((fn [g] (if (empty? c) (print (first g)) (recur c (first (rest g)) (last g))))
    ;((fn [g1 g2 g3] (if (empty? c) (print g1) (recur c g2 g3)))
    (loop [a (seq b)  ; loop through instructions in a line
           i fi
           j fj]
      ;(println " from:" (aget pad i j) " do:" a)
      (let [d (first a)
            f (rest a)]
      (case d
        \U (let [ni (if (= i 0) i (dec i))
                 nj j]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))
        \D (let [ni (if (= i mi) i (inc i))
                 nj j]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))
        \L (let [ni i
                 nj (if (= j 0) j (dec j))]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))
        \R (let [ni i
                 nj (if (= j mj) j (inc j))]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))))))))))
    ;(if (not (empty? c)) (recur c fi fj))))))    ;; this needs to be ni, nj ... in scope

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def document
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))
(println " number of lines read from input file:" (count document))

;(combo (reverse document)) (println)
(println (combo (reverse document)))             ; `reverse` b/c `conj ()` for reading file


