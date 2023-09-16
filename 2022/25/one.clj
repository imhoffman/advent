
(require '[clojure.string :as str])
;(require '[clojure.set :as set])

(defn intpow [base exponent]
  (assert (> exponent -1) "natural numbers only")
  (loop [o 1
         n exponent]
    (if (= n 0)
      o
      (recur
        (* o base)
        (dec n)))))


;(def input (->> "puzzle.txt"
;                slurp
;                (#(str/split % #"\n") ,,)))

;(prn input)


;;  snafu char lookup table
(def d {\2 2, \1 1, \0 0, \- -1, \= -2})


(defn snafu-to-dec [numeral-string]
  (let [snafu-chars (reverse numeral-string)
        n-places (count snafu-chars)]
    (loop [s snafu-chars
           n 0
           o 0]
      (if (= n n-places)
        o
        (recur
          (rest s)
          (inc n)
          (+ o (* (d (first s)) (intpow 5 n))))))))


;;
;;  my own base-changer
;;   how to do snafu modular arith...
;;
(defn dec-to-base-5 [numeral-string]
  (let [dec-val (Integer/parseInt numeral-string)]
    (println " Java thinks:" (Integer/toString dec-val 5))
    (loop [v dec-val
           n (loop [i 0] (if (< v (intpow 5 i)) (dec i) (recur (inc i))))
           s ""]
      (if (< n 0)
        s
        (let [current-base-5-digit (quot v (intpow 5 n))]
          (recur
            (- v (* current-base-5-digit (intpow 5 n)))
            (dec n)
            (str s (str current-base-5-digit))))))))


