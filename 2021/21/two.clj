
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.math.combinatorics :as combo])


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


;;  what is the max possible number of turns in a game?
(def permos
  (map vector
    (combo/permutations [1 2 3])
    (nthrest (repeat 2 (combo/permutations [1 2 3])) 1)))

(defn get-rolls [n]
  (nth (partition 20 1 (flatten (repeat permos))) n))



;;
;;  main
;;
(loop [status {:turn :p1, :s1 0 :p1 p1, :s2 0 :p2 p2}
       d {}
       num-games 0
       rolls (get-rolls 0)]
  (if (or (>= (status :s1) 21) (>= (status :s2) 21))
    (do 
      (when (= 0 (mod (count d) 10))
        (let [s1 (count (filter #(= :s1 %) (vals d)))]
          (println num-games ":" s1 (- (count d) s1))))
      (recur
        {:turn :p1, :s1 0 :p1 p1, :s2 0 :p2 p2}
        (assoc d rolls (if (>= (status :s1) 21) :s1 :s2))
        (inc num-games)
        (get-rolls (inc num-games))))
    (let [spaces (inc (rand-int 3))   ;;  random for testing
    ;(let [spaces (first rolls)
          score-to-change (if (= :p1 (status :turn)) :s1 :s2)
          sum-val (mod (+ (status (status :turn)) spaces) 10)
          new-place (if (= 0 sum-val) 10 sum-val)]
      (recur
        (apply assoc status
               (vector
                 :turn           (if (= :p1 (status :turn)) :p2 :p1)
                 (status :turn)  new-place
                 score-to-change (+ (status score-to-change) new-place)))
        d
        num-games
        (rest rolls)))))


