
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([AKQJT\d]{5}) (\d+)" %) ,,)
                (map rest ,,)
                (map #(vector (first %) (Integer/parseInt (second %))) ,,)
                (into {} ,,)))

(prn input)

(def ranks {\A 0, \K 1, \Q 2, \J 3, \T 4, \9 5, \8 6, \7 7, \6 8, \5 9, \4 10, \3 11, \2 12, \1 13})

(defn ranker [pair1 pair2]
  (let [h1 (first pair1)
        h2 (first pair2)
        f1 (frequencies h1)
        f2 (frequencies h2)
        m1 (apply max (vals f1))
        m2 (apply max (vals f2))
        v1 (vec h1)
        v2 (vec h2)]
    (prn v1 v2)
    (cond
      (> m1 m2) 1
      (< m1 m2) 0
      ;(= 2 (count (frequencies (vals 
      :default  (loop [i 0]
                  (cond
                    (and (= 1 m1) (> (apply min (map ranks v1)) (apply min (map ranks v2)))) 1
                    (and (= 1 m1) (< (apply min (map ranks v1)) (apply min (map ranks v2)))) 0
                    (> (ranks (v1 i)) (ranks (v2 i))) 1
                    (< (ranks (v1 i)) (ranks (v2 i))) 0
                    :default (recur (inc i)))))))

(println
  (sort ranker input))




