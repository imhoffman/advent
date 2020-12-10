
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(Long/parseLong %))
       sort))


(loop [stack  (conj input 0)
       ones   0
       threes 1]
  (if (empty? (rest stack))
    (println ones "*" threes "=" (* ones threes))
    (cond
      (< 3 (- (second stack) (first stack)))
        (prn "fail")
      (= 1 (- (second stack) (first stack)))
        (recur (rest stack) (inc ones) threes)
      (= 3 (- (second stack) (first stack)))
        (recur (rest stack) ones (inc threes))
      :else
        (recur (rest stack) ones threes))))


;; 2015
;; 2046


