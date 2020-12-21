
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
;;   dict entry that is an edges-vec-dict
;;   i.e., a vec of four pairs of vecs: each side and its reverse
;;
(defn make-edges-vec [tile-val]
  (let [data (str/split tile-val #"\n")]
    (vector (let [s (first data)]
              (vector s (apply str (reverse s))))
            (let [s (last data)]
              (vector s (apply str (reverse s))))
            (let [s (apply str (for [row data] (first row)))]
              (vector s (apply str (reverse s))))
            (let [s (apply str (for [row data] (last row)))]
              (vector s (apply str (reverse s)))))))


;;
;;  a dictionary of vector of the edges of the tiles
;;
(def edges-vec-dict
  (loop [ks (keys tiles-dict)
         out tiles-dict]
    (if (empty? ks)
      out
      (recur
        (rest ks)
        (assoc out
               (first ks)
               (make-edges-vec (tiles-dict (first ks))))))))

;(println edges-vec-dict)



;;
;;  any tiles that have a non-matching edge must be a corner
;;
(let [d edges-vec-dict]
  (loop [ks (keys edges-vec-dict)
         corners (vector)]
    (if (= 4 (count corners))
      (prn corners "-->" (apply * corners))
      (recur
        (rest ks)
        (let [k (first ks)
              di (dissoc d k)]
          (loop [kis (keys di)
                 sides 0
                 out corners]
            (if (empty? kis)
              (conj out k)
              (let [c (count
                        (filter true?
                                (for [v (d k)]
                                  (let [poss (set (flatten (for [tv (di (first kis))] (list (tv 0) (tv 1)))))]
                                    (or (contains? poss (v 0)) (contains? poss (v 1)))))))]
                (cond
                  (> (+ sides c) 2)
                    out
                  :else
                    (recur (rest kis) (+ sides c) out))))))))))


;;
;;  accept a tiles-dict val and return a
;;   dict entry that is an edges-named-dict val
;;
(defn make-edges-dict [tile-val]
  (let [data (str/split tile-val #"\n")]
    {:top    (first data)
     :bottom (last data)
     :left   (apply str (for [row data] (first row)))
     :right  (apply str (for [row data] (last row)))}))


;;
;;  a dictionary of the named edges of the tiles
;;
(def edges-named-dict
  (loop [ks (keys tiles-dict)
         out tiles-dict]
    (if (empty? ks)
      out
      (recur
        (rest ks)
        (assoc out
               (first ks)
               (make-edges-dict (tiles-dict (first ks))))))))

;(println edges-named-dict)


;;
;;  act on a named-dict entry and return the new val dict of the tile
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




