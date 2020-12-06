
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-list-of-lines
  ((fn [s] (->> s
                slurp
                (re-seq #"\w+")))
   "puzzle.txt"))


(defn pow [b e]
  (loop [n e
         a 1]
    (if (= n 0)
      a
      (recur (dec n) (* b a)))))


(defn id [coll set-of-hi-bits]
  (loop [stack coll
         place (dec (count stack)) 
         accum 0]
    (if (empty? stack)
      accum
      (recur
        (rest stack)
        (dec place)
        (if (contains? set-of-hi-bits (first stack))
          (+ accum (pow 2 place))
          accum)))))


(def missing-set (set/difference
       (set (range (* 127 7)))
       (set (for [e input-list-of-lines] (id e #{\B \R})))))


(loop [s missing-set]
  (let [e (first s)]
    (if (or
          (contains? missing-set (inc e))
          (contains? missing-set (dec e)))
      (recur (rest s))
      (println e))))
  

