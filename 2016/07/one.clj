(require '[clojure.string :as str])

;;  return the strings outside of the \[ \] as a vector of strings
;;   initiate the recursion by calling with p as an empty vector []
(defn get-outsides [s p]
  (let [n (if (str/index-of s \])
            (min (count s) (or (str/index-of s \[) 8192))
            (count s))]
    (if (= n (count s))
      (conj p (subs s 0 n))
      (recur (subs s (+ 1 (str/index-of s \])))
             (conj p (subs s 0 n)))
      )))

;;  same as above, but for the strings between the \[ \]
(defn get-insides [s p]
  (let [n (+ 1 (str/index-of s \[))
        m (str/index-of s \])]
    (if (not (str/index-of (subs s m) \[))
      (conj p (subs s n m))
      (recur (subs s (+ 1 m))
             (conj p (subs s n m)))
      )))

;; parse substrings from \[ and \] before calling this
(defn has-palindrome? [s i]
  (let [Ls (count s)
        Lp 4
        r  (vec (char-array s))]
    (if (< Ls 4)
      false
      (if (and
            (= (get r i)       (get r (+ i 3)))
            (= (get r (+ i 1)) (get r (+ i 2)))
            (not (= (get r i) (get r (+ i 1)))))
        true
        (recur s (+ i 1)))
      )))

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
;;   NB: lists get appended at the front; conj to a vec if you care
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))
(println "Input file has" (count input) "lines.")

(println (first input))
(println " outsides:" (get-outsides (first input) []))
(println "  insides:" (get-insides (first input) []))

(println (second input))
(println " outsides:" (get-outsides (second input) []))
(println "  insides:" (get-insides (second input) []))

(println (map (fn [a] (has-palindrome? a 0)) (get-outsides (second input) [])))

