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
;;   not necessarily a stop ... can be merely retreading
(defn revisited? [loc pv]
  (let [x (get loc 2)   ; redo for four vec ... zip up all of the traversed points and compare
        y (get loc 3)]
    (println "called with x,y:" x y)
    (doseq [p pv]
      (let [xi (get p 0)
            xf (get p 2)
            yi (get p 1)
            yf (get p 3)]
      (println " checking"xi"--"x"--"xf" and "yi"--"y"--"yf)))
  (some true?
    (for [p pv]
      (let [xi (get p 0)
            xf (get p 2)
            yi (get p 1)
            yf (get p 3)]
      (if (and (if (> xf xi) (and (>= x xi) (<= x xf)) (and (<= x xi) (>= x xf)))
               (if (> yf yi) (and (>= y yi) (<= y yf)) (and (<= y yi) (>= y yf))))
        true
        false))))))

(defn two [v]
  (loop [i  0    ; vector element
         x  0    ; latitude (N +)
         y  0    ; longitude (E +)
         b  0    ; North 0, West 1, South 2, East 3
         pv [[0 0 0 0]] ] ; history of traversals [oldx oldy newx newy]
    (if (= i (count v)) (println "got to end of directions vector :("))
    (if (revisited? (last pv) (drop-last pv))
      (+ x y)
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

