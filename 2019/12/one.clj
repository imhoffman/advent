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
  (let [x (first moons-xp)
        v (second moons-xp)]))




;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
(println "Read" (count input) "lines.")

;;  load up boundary condition
;;   vector of: vector of x, vector of xdot
(def initial-phases
  (vector 
    (vec (for [moon input] (get-xyz moon [])))
    (vec (for [_ (range 4)] (vector 0 0 0)))))

(println initial-phases)

