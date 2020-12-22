
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def two-decks
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n\n") ,,)
       (map #(str/split % #"\n") ,,)
       (map rest ,,)))

(def p1 (mapv #(Long/parseLong %) (first two-decks)))
(def p2 (mapv #(Long/parseLong %) (second two-decks)))


(defn play [deck1 deck2 game]
  (loop [d1 deck1
         d2 deck2]
    (println " Game #" game "\n" d1 d2 "\n")
    (cond
      (or (empty? d1) (empty? d2))
        (vector d1 d2)
      (and (> (count d1) (first d1)) (> (count d2) (first d2)))
        (let [[rd1 rd2] (play (vec (rest d1)) (vec (rest d2)) (inc game))]
          (if (empty? rd1)
            (let [new-d1 (vec (rest d1))
                  new-d2 (conj (vec (rest d2)) (first d2) (first d1))]
              (recur
                new-d1
                new-d2))
            (let [new-d1 (conj (vec (rest d1)) (first d1) (first d2))
                  new-d2 (vec (rest d2))]
              (recur
                new-d1
                new-d2))))
      (> (first d1) (first d2))
        (let [new-d1 (conj (vec (rest d1)) (first d1) (first d2))
              new-d2 (vec (rest d2))]
          (recur
            new-d1
            new-d2))
      (> (first d2) (first d1))
        (let [new-d1 (vec (rest d1))
              new-d2 (conj (vec (rest d2)) (first d2) (first d1))]
          (recur
            new-d1
            new-d2))
      :else
        (println "war!"))))


(let [winner (filter #(not (empty? %)) (play p1 p2 1))]
  (println (first winner))
  (println (loop [d (reverse (first winner))
                  i 1
                  accum 0]
         (if (empty? d)
           accum
           (recur
             (rest d)
             (inc i)
             (+ accum (* i (first d))))))))


;; 17923


