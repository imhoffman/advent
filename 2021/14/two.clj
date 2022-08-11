
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
(loop [di (reduce #(if (%1 %2)
                     (assoc %1 %2 (inc (%1 %2)))
                     (assoc %1 %2 1))
                  {} (map #(apply str %) (partition 2 1 template)))
       rounds 0]
  (if (= rounds 40)
    (let [df (reduce (fn [a b]
                       (let [[c1,c2] (apply vector (first b))]
                         (->> a
                              (#(assoc % c1 (+ (second b) (or (% c1) 0))) ,,)
                              (#(assoc % c2 (+ (second b) (or (% c2) 0))) ,,))))
                     {} di)
          f (->> df
                 (#(assoc % (first template) (inc (% (first template)))) ,,)
                 (#(assoc % (last template) (inc (% (last template)))) ,,)
                 (reduce (fn [d kv] (assoc d (first kv) (/ (second kv) 2))) {} ,,))]
      (println f)
      (println (- (apply max (vals f)) (apply min (vals f)))))
    ;;  else
      (recur
        (reduce (fn [d kv]
                  (let [key-pair (first kv)
                        pair-count (second kv)]
                    (if (rules key-pair)
                      (let [first-pair (apply str (first key-pair) (str (rules key-pair)))
                            second-pair (apply str (str (rules key-pair)) (str (second key-pair)))]
                        ;(println "applying" key-pair "->" (rules key-pair))
                        ;(println "need to inc" first-pair "past" (d first-pair))
                        ;(println "need to inc" second-pair "past" (d second-pair))
                        (->> d
                             (#(assoc %  first-pair (+ pair-count (or (%  first-pair) 0))) ,,)
                             (#(assoc % second-pair (+ pair-count (or (% second-pair) 0))) ,,)))
                      (do
                        ;;  this doesn't seem to happen, untested
                        (println "no rule for" key-pair "so simply inc the dict")
                        (assoc d key-pair (+ pair-count (or (d key-pair) 0)))))))
                  {} di)
        (inc rounds))))


