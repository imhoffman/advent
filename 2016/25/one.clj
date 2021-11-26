
(def offset (Integer/parseInt (first *command-line-args*)))

(loop [n 0]
  (if (> n offset)
    (println (- n offset))
    (recur (if (even? n) (+ 1 (* 2 n)) (* 2 n)))))


