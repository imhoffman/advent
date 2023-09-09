
(require '[clojure.string :as str])
;(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([a-z]{4}): ([a-z]{4}|\d+)\s?([\+\-\*\/])?\s?([a-z]{4}|\d+)?" %) ,,)
                (map rest)
                (map (fn [ss]
                       (reduce (fn [a b] (conj a (if (and b (re-matches #"\d+" b)) (Integer/parseInt b) b)))
                               [] ss)) ,,)
                (reduce #(assoc %1 (first %2) (rest %2)) {} ,,)))

;(prn input)



(defn find-value [d k]
  (let [v1 (first  (d k))
        v2 (second (d k))
        v3 (last   (d k))]
    (println d)
    (cond
      (and (number? v1) (nil? v2))
      v1

      (let [v (or (number? (d v1)) (number? (d v3)))]
      (and ((complement number?) v1) (nil? v2))
      (recur (assoc d v1 (list (d v1))) k)

      (and (number? (d v1)) (number? (d v3)))
      (find-value (assoc d k (list ((resolve (read-string v2)) (d v1) (d v3)))) k)

      :default
      (recur
        (find-value d (or ((complement number?) v1) ((complement number?) v3)))
        k))))


(println (find-value input "ptdq"))






