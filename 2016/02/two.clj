(require '[clojure.string :as str])

;;  https://clojuredocs.org/clojure.core/to-array-2d
(def pad (to-array-2d [ [nil nil \1  nil nil]
                        [nil \2  \3  \4  nil]
                        [\5  \6  \7  \8  \9 ]
                        [nil \A  \B  \C  nil]
                        [nil nil \D  nil nil] ]))


;;  part two ruleset
;;   array-search filter modularized from `combo-line`
;;   `cond` idiom https://clojure.org/guides/learn/flow#_cond
(defn move-ok? [dir y x]
  (case dir
    \U (cond (and (= x 2) (> y 0)) true
             (and (> x 0) (< x 4) (> y 1)) true
             :else false)
    \D (cond (and (= x 2) (< y 4)) true
             (and (> x 0) (< x 4) (< y 3)) true
             :else false)
    \L (cond (and (= y 2) (> x 0)) true
             (and (> y 0) (< y 4) (> x 1)) true
             :else false)
    \R (cond (and (= y 2) (< x 4)) true
             (and (> y 0) (< y 4) (< x 3)) true)
              :else false))


;;   internal functionality of recursive `combo`
;;   logic of permissible moves is flipped from `one.clj`
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
        \U (let [ni (if (move-ok? d i j) (dec i) i)
                 nj j]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))
        \D (let [ni (if (move-ok? d i j) (inc i) i)
                 nj j]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))
        \L (let [ni i
                 nj (if (move-ok? d i j) (dec j) j)]
             (if (empty? f) (list (aget pad ni nj) ni nj) (recur f ni nj)))
        \R (let [ni i
                 nj (if (move-ok? d i j) (inc j) j)]
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
     ; "horizontal" index   start on "5" at 2,0
(println (combo (reverse document) 2 0))             ; `reverse` b/c `conj ()` for reading file


