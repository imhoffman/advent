
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(Long/parseLong %) ,,)
       vec))

;(prn input)

(defn find-loop-size [public-key]
  (loop [c 0
         a 1]
    (if (= a public-key)
      c
      (recur
        (inc c)
        (mod (* a 7) 20201227)))))


;(let [[card-loop door-loop] (vec (for [k input] (find-loop-size k)))]
(let [card-loop (find-loop-size (input 0))]
  (loop [c 0
         a 1]
    (if (= c card-loop)
      (prn a)
      (recur
        (inc c)
        (mod (* a (input 1)) 20201227)))))


