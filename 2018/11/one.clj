
(def sn (Integer/parseInt (first *command-line-args*)))

(defn power-level [x y]
  (let [rack-id (+ x 10)]
    (-> rack-id
        (* ,, y)
        (+ ,, sn)
        (* ,, rack-id)
        (#(if (< 2 (count (str %)))
            (Integer/parseInt (str ((vec (reverse (str %))) 2)))
            0) ,,)
        (- ,, 5))))


;(let [x 101 
;      y 153]
;  (println x y (power-level x y)))

(def pl (memoize power-level))


(def squares-dict
  (loop [x 1
         y 1
         o {}]
    (cond
      (= x 299) (recur 1 (inc y) o)
      (= y 299) o
      :else (recur (inc x) y (assoc o
                                    (vector x y)
                                    (apply + (for [xc (range x (+ x 3))
                                                   yc (range y (+ y 3))]
                                               (pl xc yc))))))))

(let [[x,y] (key (apply max-key val squares-dict))]
  (println x y))


