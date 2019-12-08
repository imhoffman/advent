(require '[clojure.string :as str])

(def width 25)
(def height 6)
(def pixels_per_layer (* width height))




;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (str/trim (slurp f))))
(println "Read" (count input) "puzzle characters from one line.")

(def layers
  ((fn [s accum]
    (if (str/blank? s)
      accum
      (let [r (subs s 0 (dec pixels_per_layer))]
        (recur (subs s pixels_per_layer) (conj accum r)))))
  input ()))


(doseq [layer layers]
  (println layer))



