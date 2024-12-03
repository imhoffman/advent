
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map (fn [ss] (let [s (str/split ss #"\s")] (mapv #(Integer/parseInt %) s))) ,,)))

;(println input)

(defn safe? [report f]
  (let [p (if (= f >) - +)]
    (let [reports (for [i (range (count report))]
                    (apply conj (subvec report 0 i) (subvec report (inc i))))]
      (loop [vs reports
             a (first vs)
             o true]
        (if (nil? a)
          o
          (if (= 1 (count a))
            (recur [] nil true)
            (if (and (f (first a) (second a))
                     ((complement f) (p (first a) 4) (second a)))
              (recur vs (rest a) o)
              (recur (rest vs) (first (rest vs)) false))))))))


(println (count (filter #(or (safe? % <) (safe? % >)) input)))


