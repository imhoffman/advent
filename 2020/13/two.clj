
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def notes-vec
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       second
       (#(str/split % #","))
       (map #(re-matches #"\d+" %))
       (map #(if (nil? %) nil (Long/parseLong %)))
       vec))


(def notes-dict
  (loop [s notes-vec
         i 0
         out {}]
    (if (empty? s)
      out
      (recur (rest s)
             (inc i)
             (if (nil? (first s))
               out
               (assoc out i (first s)))))))


(prn notes-dict)


;(loop [t 0]
(loop [t 100000000000000]
  (when (= 0 (mod t 10000000)) (println " considering" t))
  (if (every? true? (for [e notes-dict] (= 0 (mod (+ t (key e)) (val e)))))
    (prn t)
    (recur (inc t))))








