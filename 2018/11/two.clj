
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
         sz 1
         o {}]
    (cond
      (= 301 sz) o
      ;(and (> sz 1) (> 0 (apply max (filter some? (for [xd (range 301), yd (range 301)] (o (vector xd yd (dec sz)))))))) (persistent! o)
      (= x (- 301 sz)) (recur 1 (inc y) sz o)
      (= y (- 301 sz))
        (let [[xd,yd,szd] (key (apply max-key val o))]
          (println sz)
          (println xd yd szd)
          (recur 1 1 (inc sz) o))
      :else (recur
              (inc x)
              y
              sz
              (assoc o
                      (vector x y sz)
                      (apply +
                             (for [xc (range x (+ x sz))
                                   yc (range y (+ y sz))]
                               (pl xc yc))))))))

(let [[x,y,sz] (key (apply max-key val squares-dict))]
  (println x y sz))


