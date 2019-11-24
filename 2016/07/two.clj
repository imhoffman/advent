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
      (conj p (subs s n m))       ;; duplicating this line in the recur is JV!
      (recur (subs s (+ 1 m))
             (conj p (subs s n m)))
      )))


;;  pass in `outsides` and `insides` as coll's
(defn ssl [outsides insides]
  (some true?
    (for [a outsides]
      (let [va (vec (char-array a))]
        (loop [i 0]
          (if (> (+ i 3) (count va))
            false
            (if (and
                  (= (get va i) (get va (+ i 2)))
                  (not (= (get va (+ i 1)) (get va i))))
              (let [bab (list (get va i) (get va (+ 1 i)) (get va (+ 2 i)))]
                (some true?      ;; this runs the whole loop; a reduced lambda is better
                  (for [b insides]
                    (let [vb (vec (char-array b))]
                      (loop [j 0]
                        (if (> (+ j 3)) (count vb))
                          false
                          (if (= bab (list (get vb j) (get vb (+ 1 j)) (get vb (+ 2 j))))
                            true)
                          (recur (+ 1 j)))))))
              (recur (+ 1 i)))))))))


;; parse substrings from \[ and \] before calling this
(defn has-palindrome? [s i]
  (let [Ls (count s)
        r  (vec (char-array s))]
    (if (> (+ i 4) Ls)
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

;;  add a 1 for each entry that satisfies the ruleset
(println
  " number of TLS entries:"
  (reduce +
    (for [s input]
      (if (and
              (some true? (map (fn [a] (has-palindrome? a 0)) (get-outsides s [])))
              (not (some true? (map (fn [a] (has-palindrome? a 0)) (get-insides s [])))))
        1 0))))

;;  add a 1 for each entry that satisfies the ruleset
(println
  " number of SSL entries:"
  (reduce +
    (for [s input]
      (if (ssl (get-outsides s []) (get-insides s [])) 1 0))))

(println " list of SSL's:" (for [s input] (ssl (get-outsides s []) (get-insides s []))))

