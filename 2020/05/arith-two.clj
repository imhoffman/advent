
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-list-of-lines
  ((fn [s] (->> s
                slurp
                (re-seq #"\w+")))
   "puzzle.txt"))


(defn id [s]
  (let [rows (take 7 s)
        cols (drop 7 s)]
    (+
     (* 8
        (loop [stack rows
               lo    0
               hi    127]
          (let [diff (/ (+ (- hi lo) 1) 2)]
            (if (empty? stack)
              (if (= (first stack) \F)
                lo
                hi)
              (if (= (first stack) \F)
                (recur
                  (rest stack)
                  lo
                  (- (+ lo diff) 1))
                (recur
                  (rest stack)
                  (- (+ hi 1) diff)
                  hi))))))
     (loop [stack cols
            lo    0
            hi    7]
       (let [diff (/ (+ (- hi lo) 1) 2)]
         (if (empty? stack)
           (if (= (first stack) \L)
             lo
             hi)
           (if (= (first stack) \L)
             (recur
               (rest stack)
               lo
               (- (+ lo diff) 1))
             (recur
               (rest stack)
               (- (+ hi 1) diff)
               hi))))))))


(prn (set/difference
       (set (range (* 127 7)))
       (set (for [e input-list-of-lines] (id e)))))


