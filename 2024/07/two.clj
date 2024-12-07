
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.math.combinatorics :as combo])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"(\d+): (.*)" %) ,,)
                (map rest ,,)
                (map #(vector (first %) (re-seq #"(\d+)" (second %))) ,,)
                (map (fn [s] (vector (#(Long/parseLong %) (first s)) (mapv #(Long/parseLong (first %)) (second s)))) ,,)))

;(prn input)

(defn f [a b]
  (Long/parseLong (apply str (str a) (str b))))

(defn value-if-correct [v]
  (let [answer (first v)
        terms (second v)
        operations (combo/permuted-combinations
                     (flatten (apply
                       conj
                       (take (dec (count terms)) (repeat +))
                       (take (dec (count terms)) (repeat *))
                       (take (dec (count terms)) (repeat f))))
                     (dec (count terms)))]
    (loop [ops operations]
      (cond
        (empty? ops) nil 
 
        (= answer (loop [os (first ops)
                         ts (rest terms)
                         a (first terms)]
                    (if (empty? os)
                      a
                      (recur
                        (rest os)
                        (rest ts)
                        ((first os) a (first ts))))))
        answer
  
        :else (recur (rest ops))))))


(println (apply + (filter some? (for [v input] (value-if-correct v)))))




