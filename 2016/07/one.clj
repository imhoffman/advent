(require '[clojure.string :as str])

;;  return the strings outside of the \[ \] as a vector of vectors
;;   initiate the recursion by calling with p as an empty vector []
;;   if vector indexing is not needed, then returing a subs or join'ed coll is better
(defn get-outsides [s p]
  (let [r (vec (char-array s))    ;; this is not necessary; str has indexing funcs
        n (if (str/index-of s \])
            (min (count r) (or (str/index-of s \[) 8192))
            (count r))]
    (if (= n (count r))
      (conj p (reduce conj [] (for [i (range n)] (get r i))))
      (recur (subs s (+ 1 (str/index-of s \])))
             (conj p (reduce conj [] (for [i (range n)] (get r i)))))
      )))

;;  same as above, but for the strings between the \[ \]
(defn get-insides [s p]
  (let [n (+ 1 (str/index-of s \[))
        m (str/index-of s \])]
    (if (not (str/index-of (subs s m) \[))
      (conj p (vec (char-array (subs s n m))))
      (recur (subs s (+ 1 m))
             (conj p (vec (char-array (subs s n m)))))
      )))

;; parse substrings from \[ and \] before calling this
(defn has-palindrome? [s]
  (let [L 4]
    (if (subs s 0 L) true false)))

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

