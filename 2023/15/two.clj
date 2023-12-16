
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                str/trim
                (#(str/split % #"\,") ,,)
                (map #(re-matches #"([a-z]+)([=\-])(\d{0,1})" %) ,,)
                (map rest ,,)
                (map vec ,,)))


;(prn input)

(defn hasher [string]
  (loop [s (vec string)
         c 0]
    (if (empty? s)
      c
      (recur
        (rest s)
        (->> (first s)
             (int ,,)
             (+ c ,,)
             (* 17 ,,)
             (#(mod % 256) ,,))))))


(defn my-remove [v e]
  (let [i (reduce (fn [a b] (or a (if (= e (first b)) (.indexOf v b) nil))) nil v)]
    (if i
      (into (subvec v 0 i) (subvec v (inc i)))
      v)))

(defn my-replace [v e]
  (let [i (reduce (fn [a b] (or a (if (= (first e) (first b)) (.indexOf v b) nil))) nil v)]
    (if i
      (assoc v i e)
      (conj v e))))



(defn process [instructions]
  (loop [in instructions
         o (into {} (map vector (range 256) (repeat (vector))))]
    (if (empty? in)
      o
      (let [v (first in)
            s   (v 0)
            box (hasher s)
            op  (v 1)
            f   (v 2)]
        (recur
          (rest in)
          (cond
            (= op "=") (assoc o box (my-replace (o box) (vector s f)))
            (= op "-") (assoc o box (my-remove (o box) s))
            :default   o))))))


;(doseq [s input] (prn (hasher s)))

;(println (apply + (for [s input] (hasher s))))

(println
  (let [p (process input)
        d (into {} (filter #(not (empty? (second %))) p))]
    (loop [h d
           o 0]
      (if (empty? h)
        o
        (let [[k,v] (first h)
              box (inc k)
              c (count v)]
          (recur
            (rest h)
            (+ o
               (* box (apply +
                             (map #(* (first %) (second %))
                                  (for [i (range c)] (vector (inc i) (Integer/parseInt (second (v i)))))))))))))))



