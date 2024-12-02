
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map (fn [ss] (let [s (str/split ss #"\s")] (mapv #(Integer/parseInt %) s))) ,,)))

;(println input)

(defn increasing? [v]
  (loop [a v]
    (if (= 1 (count a))
      true
      (if (and (< (first a) (second a))
               (> (+ (first a) 4) (second a)))
        (recur (rest a))
        false))))

(defn decreasing? [v]
  (loop [a v]
    (if (= 1 (count a))
      true
      (if (and (> (first a) (second a))
               (< (- (first a) 4) (second a)))
        (recur (rest a))
        false))))

(defn safe? [v]
  (or (increasing? v) (decreasing? v)))

(println (count (filter safe? input)))


