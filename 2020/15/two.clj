
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #","))
       (map #(Long/parseLong %))
       vec
       (#(apply hash-map (flatten (for [i (range (count %))] (list (% i) (inc i))))))))

(prn input)

;;
;;  hash map is key:number, val:turn at which last spoken
;;

(loop [d     input
       turn  (count d)
       prev  (key (apply max-key val d))]  ;;  key with max val
  (if (= turn 30000000)
    (prn (key (apply max-key val d)))
    (let [prev-turn (d prev)
          current   (if prev-turn (- turn prev-turn) 0)]
      (recur
        (assoc d current turn)
        (inc turn)
        current))))


