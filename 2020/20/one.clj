
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
      (prn (apply * corners))
      (recur
        (rest ks)
        (let [k (first ks)
              di (dissoc d k)]
          (loop [kis (keys di)
                 out corners]
            (cond
              (empty? kis)
                out
              (every? true?
                      (for [v (d k)]
                        (let [poss (set (flatten (for [tv (d (first kis))] (list (tv 0) (tv 1)))))]
                          (or (contains? poss (v 0)) (contains? poss (v 1))))))
                out
              :else
                (conj out k))))))))


;; 21941333976479 too high


