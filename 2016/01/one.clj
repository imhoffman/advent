(require '[clojure.string :as str])

;;  cheat by adding trailing comma
(def directions (apply str (str/trim-newline (slurp "input.txt")) ","))


;;  recursive function to obtain vector of directions
;(conj []
;  (loop [i 0
;         j 0]
;    (let [s (subs directions i)
;          b ""]
;    (for [c s]
;      (if (not (= c \,))
;        (recur i (+ 1 j))
;        (do (subs s 0 j) (recur (+ 1 i) j)))))))

(loop [a     directions
       i     0
       accum []]
  (let [j (str/index-of a \,)
        r (subs a 0 j)]
  (if (< 3 (- j (count a)))     ; b/c r=-1 will miss the last one...
    accum
    (recur (subs s (+ j 1)) (+ i j 1) (conj accum r)))))

