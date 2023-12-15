
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\,") ,,)
                (map str/trim ,,)))

;(prn input)

(defn hasher [string]
  (loop [s (vec string)
         c 0]
    (if (empty? s)
      c
      (recur
        (rest s)
        (->> (first s)
             (int ,,)
             (+ c ,,)
             (* 17 ,,)
             (#(mod % 256) ,,))))))



;(doseq [s input] (prn (hasher s)))

(println
  (apply +
         (for [s input] (hasher s))))

