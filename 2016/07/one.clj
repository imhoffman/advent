(require '[clojure.string :as str])

(defn get-outsides [s p]     ;; initially, call with p as []
  (let [r (vec (char-array s))
        n (if (< (count r) (or (str/index-of s \[) 8192)) (count r) (str/index-of s \[)) ]
    (println " n:" n ", (count r):" (count r) (subs s n))
    (if (str/blank? (subs s n))
      p
      (recur (subs s (+ 1 (str/index-of s \])))
             (conj p (reduce conj [] (for [i (range n)] (get r i)))))
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
(println (get-outsides (first input) []))


