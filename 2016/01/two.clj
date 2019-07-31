(require '[clojure.string :as str])

;;  file I/O
;;   cheat by adding trailing comma
(def directions (apply str (str/trim-newline (slurp "input.txt")) ","))

;;  recursive csv parser to load vector of instructions
;;   use vector b/c conj goes on tail and b/c order matters
(def dirvec
  (loop [a     directions
         accum [] ]
    (let [j (str/index-of a \,)    ; needs final comma added to slurp
          r (subs a 0 j)]
    (if (not (str/index-of a \space))
      (conj accum r)               ; base case ... add final entry and return
      (recur (subs a (+ j 2)) (conj accum r))))))

;(println (first dirvec) (last dirvec))
(println (count dirvec) "directions")

;;  the `rest` of a str is a seq which has to be `join`ed before `parseInt`
(defn numb [s] (Integer/parseInt (apply str (rest s))))

;;  part two ruleset
;;   for zipping up ordered pairs
(defn zipper [d]
  (let [xr (sort (list (get d 0) (get d 2)))   ; `sort` the endpoitns for zip `range`
        yr (sort (list (get d 1) (get d 3)))]
  (if (= (first xr) (last xr))    ; an x move or a y move ?
    (map vector
         (for [a (range (+ 1 (- (last yr) (first yr))))] (conj () (first xr)))
         (for [a (range (first yr) (+ 1 (last yr)))] (conj () a)))
    (map vector                   ; must be an x move; hold y constant
         (for [a (range (first xr) (+ 1 (last xr)))] (conj () a))
         (for [a (range (+ 1 (- (last xr) (first xr))))] (conj () (first yr)))))))

;;   not necessarily an intersection with a turn ... can be merely retreading
(defn get-revisit [loc pv]
  ; determine the points touched on the current leg ...
  (let [xyzip (zipper loc)]
    (reduce (fn [f g] (if g (reduced g) false))
            (for [p pv]
  ; ... and compare them to the points touched on previous legs
        (let [pxyzip (zipper p)]
          (reduce (fn [d h] (if h (reduced h) false))
                  (for [b xyzip c pxyzip]
            (if (= b c) b false))))))))

(defn two [v]
  (loop [i  0    ; vector element
         x  0    ; latitude (N +)
         y  0    ; longitude (E +)
         b  0    ; North 0, West 1, South 2, East 3
         pv [[0 0 0 0]] ] ; history of traversals [oldx oldy newx newy]
    (if (= i (count v)) (println "got to end of directions vector :("))
    (if (and (> 1 (count pv)) (get-revisit (last pv) (drop-last pv)))   ; this order mattered
      ;(get-revisit (last pv) (drop-last pv))
      8192
      (let [d (get v i)]
       (if (= (first d) \L)
         (if (= b 0) (let [x2 (- x (numb d)) y2 y] (recur (inc i) x2 y2 1 (conj pv [x y x2 y2])))
         (if (= b 1) (let [x2 x y2 (- y (numb d))] (recur (inc i) x2 y2 2 (conj pv [x y x2 y2])))
         (if (= b 2) (let [x2 (+ x (numb d)) y2 y] (recur (inc i) x2 y2 3 (conj pv [x y x2 y2])))
         (if (= b 3) (let [x2 x y2 (+ y (numb d))] (recur (inc i) x2 y2 0 (conj pv [x y x2 y2])))))))

         (if (= b 0) (let [x2 (+ x (numb d)) y2 y] (recur (inc i) x2 y2 3 (conj pv [x y x2 y2])))
         (if (= b 1) (let [x2 x y2 (+ y (numb d))] (recur (inc i) x2 y2 0 (conj pv [x y x2 y2])))
         (if (= b 2) (let [x2 (- x (numb d)) y2 y] (recur (inc i) x2 y2 1 (conj pv [x y x2 y2])))
         (if (= b 3) (let [x2 x y2 (- y (numb d))] (recur (inc i) x2 y2 2 (conj pv [x y x2 y2]))))))))))))

(println "distance to location is" (two dirvec))

