
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(Long/parseLong %))
       vec))


(defn valid? [prev-25 n]
  (loop [i1 0
         i2 1]
    (cond
      (= i1 24) false
      (= n (+ (prev-25 i1) (prev-25 i2))) true
      :else (recur
              (if (= i2 24) (inc i1) i1)
              (if (= i2 24) 0        (inc i2))))))

(loop [index 25]
  (if (valid? (subvec input (- index 25) index) (input index))
    (recur (inc index))
    (prn (input index))))


;; 14
;; 863



