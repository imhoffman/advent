
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def template
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n\n") ,,)
       first))

(def rules
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n\n") ,,)
       second
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"([A-Z]{2}) -> ([A-Z]{1})" %) ,,)
       (map #(vector (% 1) (% 2)) ,,)
       (into {} ,,)))


;;
;;  main
;;
(loop [s template
       o ""
       rounds 0]
  ;(when (= o "") (println s))
  (cond
    (= rounds 10)
      (let [f (frequencies s)]
        (println (- (apply max (vals f)) (apply min (vals f)))))
    (= (count s) 1)
      (recur
        (apply str o (str (last s)))
        ""
        (inc rounds))
    :else
      (recur
        (rest s)
        (let [pair (apply str (take 2 s))]
          (if (rules pair)
            (apply str o (str (first pair)) (rules pair))
            ;(apply str o (str (first pair)) (rules pair) (str (second pair)))
            o))
        rounds)))




