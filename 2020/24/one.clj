
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-seq #"(e|se|sw|w|nw|ne)+?" %) ,,)
       (map #(map last %) ,,)))

;(prn input)


;;
;;  using cube coords
;;   https://www.redblobgames.com/grids/hexagons/#coordinates
;;
(defn destination-coords [ds]
  (loop [stack  ds
         coords [0 0 0]]
    (if (empty? stack)
      coords
      (let [d (first stack)]
        (println " considering" d)
        (recur
          (rest stack)
          (case d
            "e"  (vector (inc (coords 0)) (dec (coords 1)) (coords 2))
            "w"  (vector (dec (coords 0)) (inc (coords 1)) (coords 2))
            "nw" (vector (coords 0) (inc (coords 1)) (dec (coords 2)))
            "se" (vector (coords 0) (dec (coords 1)) (inc (coords 2)))
            "ne" (vector (inc (coords 0)) (coords 1) (dec (coords 2)))
            "sw" (vector (dec (coords 0)) (coords 1) (inc (coords 2)))))))))


(loop [stack input
       out #{}]
  (if (empty? stack)
    (prn (count out))
    (recur
      (rest stack)
      (let [tile (destination-coords (first stack))]
        (if (contains? out tile)
          (disj out tile)
          (conj out tile))))))

