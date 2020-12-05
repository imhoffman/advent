
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


(defn bits-to-int [coll hi-bit]
  (loop [stack coll
         place (dec (count stack)) 
         accum 0]
    (if (empty? stack)
      accum
      (recur
        (rest stack)
        (dec place)
        (if (= (first stack) hi-bit)
          (+ accum (pow 2 place))
          accum)))))


(defn id [s]
  (let [rows (take 7 s)
        cols (drop 7 s)]
    (+
     (* 8
        (bits-to-int rows \B))
     (bits-to-int cols \R))))


(prn (set/difference
       (set (range (* 127 7)))
       (set (for [e input-list-of-lines] (id e)))))

