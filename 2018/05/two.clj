
(require '[clojure.string :as str])

(def polymer (->> "puzzle.txt"
                  slurp
                  butlast))

(defn react [s]
  (reduce (fn [a b]
            (let [v (re-matches #"(\w)(\w)" (str (last a) b))]
              (if (and v
                       (= (str/upper-case (v 1)) (str/upper-case (v 2)))
                       (or
                         (re-matches #"[a-z][A-Z]" (v 0))
                         (re-matches #"[A-Z][a-z]" (v 0))))
                (apply str (butlast a))
                (conj (vec (apply str a)) b))))
          (vector (first s))
          (rest s)))


(loop [stack "abcdefghijklmnopqrstuvwxyz"
       out {}]
  (if (empty? stack)
    (do (println out) (println (apply min (vals out))))
    (let [s (apply str (remove #(or (= % (first stack)) (= (str %) (str/upper-case (first stack)))) polymer))]
      (recur
        (rest stack)
        (assoc out (first stack) (count (react s)))))))



