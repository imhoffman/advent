
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


(defn play [deck1 deck2]
  (loop [d1 deck1
         d2 deck2
         h  #{}]
    (if (contains? h (vector d1 d2))
      (do
        (println "Short-circuit.")
        (vector d1 d2 true))
      (let [history (conj h (vector d1 d2))]
    (cond
      (or (empty? d1) (empty? d2))
        (vector d1 d2 false)
      (and (> (count d1) (first d1)) (> (count d2) (first d2)))
        (let [[rd1 rd2 p1-win] (play (vec (rest d1)) (vec (rest d2)))]
          (if (or p1-win (empty? rd2))
            (let [new-d1 (conj (vec (rest d1)) (first d1) (first d2))
                  new-d2 (vec (rest d2))]
              (recur
                new-d1
                new-d2
                (conj history (vector d1 d2))))
            (let [new-d1 (vec (rest d1))
                  new-d2 (conj (vec (rest d2)) (first d2) (first d1))]
              (recur
                new-d1
                new-d2
                (conj history (vector d1 d2))))))
      (> (first d1) (first d2))
        (let [new-d1 (conj (vec (rest d1)) (first d1) (first d2))
              new-d2 (vec (rest d2))]
          (recur
            new-d1
            new-d2
            (conj history (vector d1 d2))))
      (> (first d2) (first d1))
        (let [new-d1 (vec (rest d1))
              new-d2 (conj (vec (rest d2)) (first d2) (first d1))]
          (recur
            new-d1
            new-d2
            (conj history (vector d1 d2))))
      :else
        (println "war!"))))))


(let [return (play p1 p2)
      winner (if (return 2)
               (return 0)
               (first (filter #(not (empty? %)) (list (return 0) (return 1)))))]
  (println winner)
  (println (loop [d (reverse winner)
                  i 1
                  accum 0]
         (if (empty? d)
           accum
           (recur
             (rest d)
             (inc i)
             (+ accum (* i (first d))))))))


;; 17923
;; 9353 too low


