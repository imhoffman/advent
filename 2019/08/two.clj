(require '[clojure.string :as str])

(def width 25)
(def height 6)
(def pixels_per_layer (* width height))

;;  rather than `aget` within the other functions
(defn get-pixel [layers x y z]
  (get (get (get layers z) y) x))

;;  initially call with z=0, i.e. looking down from the top
(defn display-pixel [cube x y z]
  (let [current-layer-value (get-pixel cube x y z)]
    (if (= current-layer-value \2)
      (recur cube x y (inc z))
      current-layer-value)))

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
  (vec
    (for [layer layers]
      ((fn [s accum]
         (if (str/blank? s)
           accum
           (recur (subs s width) (conj accum (subs s 0 width)))))
       layer []))))


;; colours and #/. as per 2016 day 8
(def display
  (vec
    (for [y (range height)] (conj []
      (for [x (range width)]
        (if (= (display-pixel layers-2d x y 0) \1)
          "\033[31m\033[1m\033[43m#\033[0m" \.))))))

;; pretty print
(doseq [line display]
    (println (map str/join line)))


