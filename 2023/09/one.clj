
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-seq #"-?\d+" %) ,,)
                (map (fn [s] (mapv #(Long/parseLong %) s)) ,,)))

;(prn input)


(defn make-diffs [v]
  (reduce (fn [a b] (let [pair (vec b)] (conj a (- (pair 1) (pair 0))))) (vector) (partition 2 1 v)))


(defn predict [v]
  (loop [lasts [(last v)]
         diffs (make-diffs v)]
    (if (every? #(= 0 %) diffs)
      (apply + lasts)
      (recur
        (conj lasts (last diffs))
        (make-diffs diffs)))))


(println
  (apply + (map predict input)))


;;  2022497258 wrong


