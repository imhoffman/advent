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

;;  parse individual layers from input into a vector
(def layers
  ((fn [s accum]
    (if (str/blank? s)
      accum
      (let [r (subs s 0 pixels_per_layer)]
        (recur (subs s pixels_per_layer) (conj accum r)))))
  input []))

;;  2D array for display
(def layers-2d
  (vector
          (for [layer layers]
            ((fn [s accum]
               (if (str/blank? s)
                 accum
                 (recur (subs s width) (conj accum (subs s 0 width)))))
             layer []))))

