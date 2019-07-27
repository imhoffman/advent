(require '[clojure.string :as str])

(def constants {
   :alpha "abcdefghijklmonpqrstuvwxyz",
   :nums "0123456789",
   :max_lines 8192
   } )

(def registry
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))

;(println (count registry))

;;  using `reduce` rather than a list comprehension allows the use of `reduced` to bail out
;;  rather than testing a mutable in a `while` or using a CL-like `return-from`
;;  and, `reduce` seems to send in two arguments, so I'm catching both `a` and `b` but only using `b`
(defn first-digit [s]
  (reduce (fn [a b] (if (> (or (str/index-of (constants :nums) b) -2) -1) (reduced (str/index-of s b)))) s))

(defn last-digit [s]
  (reduce (fn [a b] (if (> (or (str/index-of (constants :nums) b) -2) -1) (reduced (str/last-index-of s b (- (count s) 1))))) (reverse s)))

(defn get-id [s] (Integer/parseInt (subs s (first-digit s) (+ 1 (last-digit s)))))

