
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map vec)
       (map (fn [row] (mapv #(Integer/parseInt (str %)) row)) ,,)
       (vec)))


(def nrows (count input))
(def ncols (count (input 0)))


(defn check-height [r c o]
  (let [curr ((input r) c)]
    (cond
      (= r 0)
      (cond
        (= c 0)
        (if (and (> ((input (inc r)) c) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o)

        (= c (dec ncols))
        (if (and (> ((input (inc r)) c) curr)
                 (> ((input r) (dec c)) curr))
          (assoc o (vector r c) curr)
          o)

        :else
        (if (and (> ((input (inc r)) c) curr)
                 (> ((input r) (dec c)) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o))

      (= r (dec nrows))
      (cond
        (= c 0)
        (if (and (> ((input (dec r)) c) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o)

        (= c (dec ncols))
        (if (and (> ((input (dec r)) c) curr)
                 (> ((input r) (dec c)) curr))
          (assoc o (vector r c) curr)
          o)

        :else
        (if (and (> ((input (dec r)) c) curr)
                 (> ((input r) (dec c)) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o))

      (= c 0)
      (if (and (> ((input (dec r)) c) curr)
               (> ((input r) (inc c)) curr)
               (> ((input (inc r)) c) curr))
        (assoc o (vector r c) curr)
        o)

      (= c (dec ncols))
      (if (and (> ((input (dec r)) c) curr)
               (> ((input r) (dec c)) curr)
               (> ((input (inc r)) c) curr))
        (assoc o (vector r c) curr)
        o)

      :else
      (if (and (> ((input (dec r)) c) curr)
               (> ((input r) (inc c)) curr)
               (> ((input r) (dec c)) curr)
               (> ((input (inc r)) c) curr))
        (assoc o (vector r c) curr)
        o))))



(def min-dict
  (loop [row 0
         col 0
         out {}]
    (cond
      (= row nrows)
      out

      (= col ncols)
      (recur (inc row) 0 out)

      :else
      (recur row (inc col) (check-height row col out)))))



;;
;;  this gets called only if r,c is not a minimum
;;  user provide search-list that has been scrubbed for corners/edges
;;
(defn find-lowest-neighbor [r c search-list]
  (let [neighbor-search-d (reduce #(let [rr (%2 0)
                                         cr (%2 1)
                                         m ((input (%2 0)) (%2 1))]
                                     (if (> (%1 :min) m)
                                       (assoc %1 :r rr, :c cr, :min m)
                                       %1))
                                  {:r nil, :c nil, :min 10}
                                  (filter #(> 9 ((input (% 0)) (% 1))) search-list))
        nextr (neighbor-search-d :r) 
        nextc (neighbor-search-d :c)]
    (vector nextr nextc)))




;;
;;  accept the min-dict and populates
;;   its vals with puzzle "sizes" as
;;   accumulated recursively here in
;;   the set s
;;
(defn seek-min [r c d s]
  (let [curr ((input r) c)]
    (cond

      (= 9 curr) d

      ;;  found a minimum, assoc its size set
      (contains? d (vector r c))
      (assoc d (vector r c) (set/union (d (vector r c)) s))

      (= r 0)
      (cond
        (= c 0)
        (let [[nextr nextc] (find-lowest-neighbor r c
                                                  (list
                                                    (vector (inc r) c)
                                                    (vector r (inc c))))]
          (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c)))))


        (= c (dec ncols))
        (let [[nextr nextc] (find-lowest-neighbor r c
                                                  (list
                                                    (vector (inc r) c)
                                                    (vector r (dec c))))]
          (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c)))))

        :else
        (let [[nextr nextc] (find-lowest-neighbor r c
                                                  (list
                                                    (vector (inc r) c)
                                                    (vector r (dec c))
                                                    (vector r (inc c))))]
          (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c))))))

      (= r (dec nrows))
      (cond
        (= c 0)
        (let [[nextr nextc] (find-lowest-neighbor r c
                                                  (list
                                                    (vector (dec r) c)
                                                    (vector r (inc c))))]
          (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c)))))

        (= c (dec ncols))
        (let [[nextr nextc] (find-lowest-neighbor r c
                                                  (list
                                                    (vector (dec r) c)
                                                    (vector r (dec c))))]
          (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c)))))

        :else
        (let [[nextr nextc] (find-lowest-neighbor r c
                                                  (list
                                                    (vector (dec r) c)
                                                    (vector r (inc c))
                                                    (vector r (dec c))))]
          (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c))))))

      (= c 0)
      (let [[nextr nextc] (find-lowest-neighbor r c
                                                (list
                                                  (vector (dec r) c)
                                                  (vector r (inc c))
                                                  (vector (inc r) c)))]
        (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c)))))

      (= c (dec ncols))
      (let [[nextr nextc] (find-lowest-neighbor r c
                                                (list
                                                  (vector (dec r) c)
                                                  (vector r (dec c))
                                                  (vector (inc r) c)))]
        (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c)))))

      :else
      (let [[nextr nextc] (find-lowest-neighbor r c
                                                (list
                                                  (vector (dec r) c)
                                                  (vector r (dec c))
                                                  (vector r (inc c))
                                                  (vector (inc r) c)))]
        (if (nil? nextr) d (seek-min nextr nextc d (conj s (vector r c))))))))



;;
;;  main
;;
(let [empty-min-dict (reduce #(let[[k _] %2] (assoc %1 k #{})) {} min-dict)]
  (loop [row 0
         col 0
         out empty-min-dict]
    (cond
      (= row nrows)
      (println (reduce #(* (inc (count %2)) %1) 1 (take 3 (reverse (sort-by count (vals out))))))

      (= col ncols)
      (recur (inc row) 0 out)

      :else
      (recur row (inc col) (seek-min row col out #{})))))



