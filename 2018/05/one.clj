
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


(println (count (react polymer)))



