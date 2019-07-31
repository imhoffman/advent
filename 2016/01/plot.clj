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

;;  the `rest` of a str is a seq which has to be `join`ed before `parseInt`
(defn numb [s] (Integer/parseInt (apply str (rest s))))

(defn walker [v]
  (loop [i 0    ; vector element
         x 0    ; latitude (N +)
         y 0    ; longitude (E +)
         b 0]   ; North 0, West 1, South 2, East 3
    (println x y)
    (let [d (get v i)]
    (if (= i (count v))    ; arrive here after `inc`
      (+ x y)
      (if (= (first d) \L)
         (if (= b 0) (recur (inc i) (- x (numb d)) y              1)
         (if (= b 1) (recur (inc i) x              (- y (numb d)) 2)
         (if (= b 2) (recur (inc i) (+ x (numb d)) y              3)
         (if (= b 3) (recur (inc i) x              (+ y (numb d)) 0)))))

         (if (= b 0) (recur (inc i) (+ x (numb d)) y              3)
         (if (= b 1) (recur (inc i) x              (+ y (numb d)) 0)
         (if (= b 2) (recur (inc i) (- x (numb d)) y              1)
         (if (= b 3) (recur (inc i) x              (- y (numb d)) 2))))))))))

;(defn walker [v]
;  (reduce + (for [d v]
;    (fn [d]

;(println (walker dirvec))
(walker dirvec)

