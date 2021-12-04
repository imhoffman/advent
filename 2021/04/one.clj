
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def inputs
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n\n") ,,)
       vec))

(def calls
  (->> (first inputs)
       (#(str/split % #",") ,,)
       (mapv #(Integer/parseInt %) ,,)))

(def boards
  (->> (rest inputs)
       (mapv #(str/split % #"\n") ,,)
       (mapv (fn [board] (mapv #(str/split (str/trim %) #"\s+") board)) ,,)
       (mapv (fn [board] (mapv (fn [row] (mapv #(Integer/parseInt %) row)) board)) ,,)))

(def board-dicts
  (->> boards
       (map #(hash-map :board %, :hits {}) ,,)))


;(println calls)
;(println boards)


(defn mark-board [call board-dict]
  (let [b    (board-dict :board)
        cols (count b)
        rows (count (first b))]
    (loop [r 0
           c 0]
      (cond
        (= r rows)
          board-dict
        (= call ((b r) c))
          (assoc board-dict :hits (assoc (board-dict :hits) call (vector r c)))
        (< c (dec cols))
          (recur r (inc c))
        :else
          (recur (inc r) 0)))))


(defn winner? [board-dict]
  (let [b    (board-dict :board)
        cols (count b)
        rows (count (first b))]
    (loop [r 0
           c 0]
      (cond
        (= r rows) false
        (= cols (count (filter #(= r (first %)) (vals (board-dict :hits))))) true  ;; across
        :else nil))))   ;; still need down


(defn score [board-dict call]
  (* call
     (apply + (remove
                #(contains? (board-dict :hits) %)
                (flatten (board-dict :board))))))



;;
;;  play the game
;;
(loop [cs calls
       bds board-dicts
       last-call nil]
  ;(doseq [bd bds] (println (bd :hits)))
  (let [winner (first (filter some? (for [bd bds] (if (winner? bd) bd nil))))]
    (if winner
      (println (score winner last-call))
      (recur
        (rest cs)
        (map #(mark-board (first cs) %) bds)
        (first cs)))))






