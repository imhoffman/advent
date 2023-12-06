
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-seq #"\d+" %) ,,)
                (map #(Long/parseLong (apply str %)) ,,)
                vector))

(prn input)

(defn successful-cases [pair]
  (let [d (second pair)]
    (loop [times (range 1 (first pair))
           c 0]
      (if (empty? times)
        c
        (let [t (first times)]
          (recur
            (rest times)
            (if (< d (* (- (first pair) t) t)) (inc c) c)))))))

(println
         (apply * (for [pair input] (successful-cases pair))))



