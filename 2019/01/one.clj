;;   2019 Advent Day 01
;;
;;  subprograms
;;
;;   using only integer arithmetic
(defn fuel [mass]
  (- (/ (- mass (mod mass 3)) 3) 2))

;;   `floor`ing the float that comes from the division
;;   although beware that clojure retains rationals
(defn fuel-java-floor [mass]
  (long (- (Math/floor (/ mass 3)) 2)))    ;; clojure integers are Java Longs
  ;(int (- (Math/floor (/ mass 3)) 2)))    ;; if you really wanted a Java Int


;; recursive function that traverses the input list itself
;;  called initially with accum equal to zero
;;  https://clojure.org/guides/learn/flow#_defn_and_recur
(defn total-fuel [list-of-masses accum]
  (if (empty? list-of-masses)
    accum
    (recur (rest list-of-masses) (+ accum (fuel (first list-of-masses))))))

;;  or, to completely black-box the accumulator into
;;  the procedure code a la SICP
;;  https://mitpress.mit.edu/sites/default/files/sicp/full-text/book/book-Z-H-11.html#footnote_Temp_46
(defn total-fuel-scope-block [list-of-masses]
  ((fn [work-list accum]
     (if (empty? work-list)
       accum
       (recur (rest work-list) (+ accum (fuel (first work-list))))))
   list-of-masses 0))

;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))      ;; lists add to the beginning
;    (reduce conj [] (line-seq f))))      ;; vectors add to the end

(println "Read" (count input) "lines.")

;;  comprehend the list of string inputs as parsed integers
(def input-as-ints
  (for [line input]
    (Long/parseLong line)))    ;; clojure integers are Java Longs
    ;(Integer/parseInt line))) ;; if you really wanted a Java Int
;; end of file I/O and parsing


;;
;;  puzzle solution
;;

;;   one way to do it
;;    comprehend the list of masses as a list of fuels, then
;;    add up all the members of the list
;;    https://clojuredocs.org/clojure.core/apply
(println
  " part one: the fuel needed for the module masses is"
  (apply + (for [mass-of-module input-as-ints] (fuel mass-of-module))))


;;   another way to do it
;;    use the first function defined among the subprograms
(println
  " part one: the fuel needed for the module masses is"
  (total-fuel input-as-ints 0))


;;   another way to do it
;;    the SICP-style code block function (no explicit pass of the accumulator)
(println
  " part one: the fuel needed for the module masses is"
  (total-fuel-scope-block input-as-ints))

