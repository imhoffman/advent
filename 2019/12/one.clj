(require '[clojure.string :as str])

;;  `accum` is an empty vec to start
(defn get-xyz [moon accum]
  (if (not (str/index-of moon \=))
    accum
    (let [i (str/index-of moon \=)
          j (if (str/index-of moon \,)
              (str/index-of moon \,) (str/index-of moon \>))]
      (recur (subs moon (inc j))
             (conj accum (Integer/parseInt (subs moon (inc i) j)))))))
                                        


;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
(println "Read" (count input) "lines.")

(def initial-phases
  (vec (for [moon input] (get-xyz moon []))))

(println initial-phases)

