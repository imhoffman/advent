
(require '[clojure.string :as str])

;;  run as `clj -M one.clj 10` to reproduce the example
(def magic-number (Integer/parseInt (first *command-line-args*)))


(defn grid-square [x y & n]
  (let [m (if (empty? n) magic-number (first n))
        z (-> (* x x)
              (+ ,, (* 3 x))
              (+ ,, (* 2 x y))
              (+ ,, y)
              (+ ,, (* y y))
              (+ ,, m))]
    (even? ((frequencies (Integer/toString z 2)) \1))))

;;  memoize for brute-forcing the maze
(def gsm (memoize grid-square))


(defn print-grid [xmax ymax]
  (loop [y 0]
    (if (> y ymax)
      :done
      (do
        (doseq [x (range (inc xmax))] (print (if (gsm x y) "." "#")))
        (println)
        (recur (inc y))))))


(print-grid 9 6)


