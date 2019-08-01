(require '[clojure.string :as str])

;;  https://clojuredocs.org/clojure.core/to-array-2d
(def pad (to-array-2d [ [1 2 3] [4 5 6] [7 8 9] ]))


;;  part one ruleset
;;   internal functionality of recursive `combo`
(defn combo-line [b ii ij]
  (let [mi (- (alength (aget pad 0)) 1)    ; max index dimensions on keypad
        mj (- (alength pad) 1)]
    (loop [a (seq b)                       ; loop through instructions in a line
           i ii
           j ij]
      (let [d (first a)
            f (rest a)]
      ;(println " about to move" d "from" (aget pad i j))
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
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj))))))))


;;   prints the numbers in the combo one at a time 
(defn combo [s i j]
  (let [b (first s)            ; loop through lines of instructions
        c (rest s)]
  ;(println " considering line:" b)
  (let [g (combo-line b i j)]
    (println (first g))
    (if (not (empty? c)) (combo c (first (rest g)) (last g))))))


;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def document
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))
(println " number of lines read from input file:" (count document))

     ; "vertical" index on keypad
     ; "horizontal" index   start on "5" at 1,1
(println (combo (reverse document) 1 1))             ; `reverse` b/c `conj ()` for reading file


