
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)))
                ;(reduce (fn [a b] (assoc a (Integer/parseInt (second b)) (nth b 2))) {} ,,)))

;(println input)

(defn make-sets [s]
  (let [v       (vec s)
        [sd,ss] (loop [i 0, digits #{}, symbols #{}]
                  (if (= i (dec (count s)))
                    (vector digits symbols)
                    (recur
                      (inc i)
                      (if (re-matches #"\d" (str (v i))) (conj digits i) digits)
                      (if (re-matches #"[^\d\.]" (str (v i))) (conj symbols i) symbols))))]
    {:digits sd, :symbols ss}))


(def line-dict
  (loop [i 0, x input, o {}]
    (if (= i (dec (count input)))
      o
      (recur (inc i) (rest x) (assoc o i (make-sets (first x)))))))

(defn make-diagonals [s]
  (into #{} (flatten (for [n s] (list (dec n) n)))))
  ;(into #{} (flatten (for [n s] (list (dec n) n (inc n))))))

;(println line-dict)


(defn make-substring [s n]
  (let [v (vec s)
        leading-period (inc (loop [i n] (if (or (= 0 i) (= (v i) \.)) i (recur (dec i)))))
        trailing-period (loop [i (inc n)] (if (or (<= (count v) i) (= (v i) \.)) i (recur (inc i))))]
    (subs s
          (if (and (= 1 leading-period) (not= (v 0) \.)) (dec leading-period) leading-period)
          trailing-period)))

(println
  (apply +
         (for [i (range 1 (count line-dict))]
           (let [symbols (make-diagonals ((line-dict i) :symbols))]
             (apply +
                    (apply conj
                           (map #(Integer/parseInt %) (into #{} (filter #(not= "" %) (for [j symbols] (make-substring (nth input (dec i)) j)))))
                           (map #(Integer/parseInt %) (into #{} (filter #(not= "" %) (for [j symbols] (make-substring (nth input (inc i)) j)))))))))))

(println
  (apply +
         (flatten (for [i (range (count line-dict))]
           (map #(Integer/parseInt %)
                (into #{} (apply conj
                                 (map #(if (some? %) (second %) "0") (re-seq #"(\d+)[^\d\.]" (nth input i)))
                                 (map #(if (some? %) (second %) "0") (re-seq #"[^\d\.](\d+)" (nth input i))))))))))


;; 516051 too low

;;  the sets will undercount if the same number appears more than once on the same line, etc.


