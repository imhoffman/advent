(require '[clojure.string :as str])

;;  https://clojure.org/guides/learn/hashed_colls#_looking_up_by_key
(def constants {
   :alpha "abcdefghijklmnopqrstuvwxyz",
   :nums "0123456789",
   } )

;;  using `reduce` rather than a list comprehension allows the use of `reduced` to bail out
;;  rather than testing a mutable in a `while` or using a CL-like `return-from`
;;  and, `reduce` seems to send in two arguments, so I'm catching both `a` and `b` but only using `b`

(defn first-digit [s]
  (reduce
    (fn [a b]
      (if (> (or (str/index-of (constants :nums) b) -2) -1)  ;; `or` to ensure number for `>`
        (reduced (str/index-of s b))))
    s))

(defn last-digit [s]
  (reduce
    (fn [a b]
      (if (> (or (str/index-of (constants :nums) b) -2) -1)
        (reduced (str/last-index-of s b (- (count s) 1)))))
    (reverse s)))

(defn get-id [s] (Integer/parseInt (subs s (first-digit s) (+ 1 (last-digit s)))))

;;
;; checksum verification
;;
(defn get-checksum [s]
  (subs s
        (+ 1 (str/index-of s \[))
        (str/index-of s \])))

(defn encrypted [s]
  (subs s 0 (- (first-digit s) 1)))

;;  checksum ruleset
;;   zipping up the comparison pairs is `map vector` ... as per
;;   https://stackoverflow.com/questions/2588227/is-there-an-equivalent-for-the-zip-function-in-clojure-core-or-contrib
;;   the `every` test here and in `sumcheck` could be short-circuited with a `reduce`--`reduced` pair
;;   `and` is lazy, but why doesn't this compile ?  `(apply and true '(true false))`
(defn ruleset [checksum freqs]
  (let [ra (seq (subs checksum 0 (- (count checksum) 1)))
        rb (seq (subs checksum 1))
        rp (map vector ra rb)]  ; zip
  (every? true?
    (for [p rp] (let [a (first p) b (last p) fa (get freqs a) fb (get freqs b)]
       (if (or (> fa fb)
               (and (= fa fb)
                    (< (str/index-of (constants :alpha) a)
                       (str/index-of (constants :alpha) b))))
        true false))))))

;; parser and impossibility filter
;;  https://clojuredocs.org/clojure.core/frequencies
(defn sumcheck [s]
  (let [checksum (get-checksum s)
        freqs    (frequencies (encrypted s))]
  (if (and
    (every? true? (for [c (seq checksum)] (contains? freqs c)))  ; `and` is lazy
    (ruleset checksum freqs))  ; proceed since all letters are present
   true false)))

;;
;;  decryption
;;
;;   ruleset
(defn caesar [c id]
  (let [nlet (count (constants :alpha))
        rot  (mod id nlet)]
  (if (= c \-)
    \space
    (let [i    (str/index-of (constants :alpha) c)
          tot  (+ i rot)]
    (if (> tot (- nlet 1))
      (subs (constants :alpha) (- tot nlet) (+ 1 (- tot nlet )))
      (subs (constants :alpha) tot (+ 1 tot)))))))

(defn decrypt [s]
  (let [id (get-id s)]
  ;(apply str (for [a (seq (encrypted s))] (caesar a id)))))    ; this works too
  (str/join (for [a (seq (encrypted s))] (caesar a id)))))

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def registry
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))
;(println (count registry))

;;  part two: print all decrypted names ... or filter with sumcheck
;(doseq [s registry] (println (get-id s) (decrypt s)))
;(doseq [s registry] (if (not (sumcheck s)) (println (get-id s) (decrypt s))))
(doseq [s registry] (if (sumcheck s) (println (get-id s) (decrypt s))))

;;  part one sum
(println " Total = "
 (reduce + (for [s registry] (if (sumcheck s) (get-id s) 0))))

