
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
       (mapv (fn [e] (mapv #(str/split % #"\s+") e)) ,,)
       (mapv (fn [rows] (for [row rows] (mapv #(Integer/parseInt %) row)) rows) ,,)))

(def board-dicts
  (->> boards
       (map #(hash-map :board %, :hits (list)) ,,)))


;(println calls)
;(println boards)


(defn mark-board [call board-dict]
  (if (some #(= call %) (flatten (board-dict :board)))
    (assoc board-dict :hits (conj (board-dict :hits) call))
    board-dict))


(defn winner? [board call]
  true)


(loop [cs calls
       bds board-dicts]
  (if (some true? (for [bd bds] (winner? bd (first cs))))
    ;; return the winner and the recent call
    (list (filter #(winner? % (first cs)) bds) (first cs))
    (recur
      (rest cs)
      (map #(mark-board (first cs) %) bds))))






