
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def p1
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       first
       (re-matches #"Player [12]{1} starting position: (\d{1,2})" ,,)
       (#(Integer/parseInt (second %)) ,,)))

(def p2
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       second
       (re-matches #"Player [12]{1} starting position: (\d{1,2})" ,,)
       (#(Integer/parseInt (second %)) ,,)))


(defn roll [last-n]
  (let [final (mod (+ 3 last-n) 100)
        sum   (+ (mod (+ 1 last-n) 100) 
                 (mod (+ 2 last-n) 100) 
                 final)]
    (vector sum final)))
                 


;;
;;  main
;;
(loop [status {:turn :p1, :s1 0 :p1 p1, :s2 0 :p2 p2}
       last-roll 0
       num-rolls 0]
  (if (or (>= (status :s1) 1000) (>= (status :s2) 1000))
    (let [loser (min (status :s1) (status :s2))]
      (println loser "*" num-rolls "=" (* num-rolls loser)))
    (let [[spaces,die] (roll last-roll)
          score-to-change (if (= :p1 (status :turn)) :s1 :s2)
          sum-val (mod (+ (status (status :turn)) spaces) 10)
          new-place (if (= 0 sum-val) 10 sum-val)]
      (recur
        (apply assoc status
               (vector
                 :turn           (if (= :p1 (status :turn)) :p2 :p1)
                 (status :turn)  new-place
                 score-to-change (+ (status score-to-change) new-place)))
        die
        (+ 3 num-rolls)))))





