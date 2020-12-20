
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def tiles-dict
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n\n") ,,)
       (map #(re-matches #"Tile (\d+):\n([\s\S]*)" %) ,,)
       (map rest ,,)
       (map #(list (Long/parseLong (first %)) (rest %)) ,,)
       flatten
       (apply hash-map ,,)))

;(println tiles-dict)


;;
;;  accept a tiles-dict val and return a
;;   dict entry that is an edges-dict val
;;
(defn bundle-edges [tile-val]
  (let [data (str/split tile-val #"\n")]
    {:top    (first data)
     :bottom (last data)
     :left   (apply str (for [row data] (first row)))
     :right  (apply str (for [row data] (last row)))}))


;;
;;  a dictionary of the edges of the tiles
;;
(def edges-dict
  (loop [ks (keys tiles-dict)
         out tiles-dict]
    (if (empty? ks)
      out
      (recur
        (rest ks)
        (assoc out
               (first ks)
               (bundle-edges (tiles-dict (first ks))))))))

;(println edges-dict)


;;
;;  return the new val dict of the tile
;;
(defn rotate [tile-val]
  {:top    (tile-val :left)
   :bottom (tile-val :right)
   :left   (tile-val :bottom)
   :right  (tile-val :top)})

(defn flip-tb [tile-val]
  {:top    (tile-val :bottom)
   :bottom (tile-val :top)
   :left   (apply str (reverse (tile-val :left)))
   :right  (apply str (reverse (tile-val :right)))})

(defn flip-rl [tile-val]
  {:top    (apply str (reverse (tile-val :top)))
   :bottom (apply str (reverse (tile-val :bottom)))
   :left   (tile-val :right)
   :right  (tile-val :left)})


;;
;;  now, search all the permutations...
;;


