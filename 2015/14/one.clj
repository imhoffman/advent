
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def total-time
  (if *command-line-args*
    (Integer/parseInt (first *command-line-args*))
    2503))


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"^(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.$" %) ,,)
       (reduce (fn [a b] (assoc a (second b) (into {} (map vector [:speed :flight-time :rest-time] (nthrest b 2))))) {} ,,)))


(defn distance [d t]
  (let [v (Integer/parseInt (d :speed))
        f (Integer/parseInt (d :flight-time))
        r (Integer/parseInt (d :rest-time))
        T (+ f r)]
    (+ (* v f (quot t T))
       (* v (min f (mod t T))))))



;;
;;  main
;;

(println
  (apply max (vals (reduce (fn [a b] (assoc a (first b) (distance (second b) total-time))) {} input))))


