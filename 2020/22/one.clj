
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def two-decks
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n\n") ,,)
       (map #(str/split % #"\n") ,,)
       (map rest ,,)))

;(println two-decks)

(def p1 (mapv #(Long/parseLong %) (first two-decks)))
(def p2 (mapv #(Long/parseLong %) (second two-decks)))


(defn play [deck1 deck2]
  (loop [d1 deck1
         d2 deck2]
    (cond
      (empty? d1)
        (prn (loop [d (reverse d2), i 1, accum 0] (if (empty? d) accum (recur (rest d) (inc i) (+ accum (* i (first d)))))))
      (empty? d2)
        (prn (loop [d (reverse d1), i 1, accum 0] (if (empty? d) accum (recur (rest d) (inc i) (+ accum (* i (first d)))))))
      (> (first d1) (first d2))
        (recur
          (conj (vec (rest d1)) (first d1) (first d2))
          (vec (rest d2)))
      (> (first d2) (first d1))
        (recur
          (vec (rest d1))
          (conj (vec (rest d2)) (first d2) (first d1)))
      :else
        (println "war!"0))))


(play p1 p2)


