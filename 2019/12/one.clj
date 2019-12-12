(require '[clojure.string :as str])

;; for parsing positions from lines in input file
;;  `accum` is an empty vec to start
(defn get-xyz [moon accum]
  (if (not (str/index-of moon \=))
    accum
    (let [i (str/index-of moon \=)
          j (if (str/index-of moon \,)
              (str/index-of moon \,) (str/index-of moon \>))]
      (recur (subs moon (inc j))
             (conj accum (Integer/parseInt (subs moon (inc i) j)))))))


(defn find-vel [moons-xp]
  (let [moon-x (first moons-xp)
        moon-v (second moons-xp)]
    (for [moon moon-x]
       ;; compute self-pair since no Dvel
      (for [pair1 (range 4)] (for [pair2 (range 4)]
        (for [axis (range 3)]
          (do
            (when (> (get (get moon pair1) axis) (get (get moon pair2) axis) 1))
            (when (< (get (get moon pair1) axis) (get (get moon pair2) axis) -1))
            (when (= (get (get moon pair1) axis) (get (get moon pair2) axis) 0)))))))))



;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
(println "Read" (count input) "lines.")

;;  load up boundary condition
;;   array of: array of x, array of xdot ... arrays are dirty here
(def initial-phases
  (into-array (vector
    (into-array (vec (for [moon input] (get-xyz moon []))))
    (into-array (vec (for [_ (range 4)] (vector 0 0 0)))))))

(println (get (aget initial-phases 0 3) 1))


