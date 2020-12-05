
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


(prn (set/difference
       (set (range (* 127 7)))
       (set (for [e input-list-of-lines] (id e #{\B \R})))))

