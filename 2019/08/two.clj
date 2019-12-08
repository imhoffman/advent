(require '[clojure.string :as str])

(def width 25)
(def height 6)
(def pixels_per_layer (* width height))

;;  "array" addressing
;;  rather than `aget` within actual Java arrays
(defn get-pixel [layers x y z]
  (get (get (get layers z) y) x))

;;  initially call with z=0, i.e. looking down from the top
;;   recursively searches vertically past transparent values
(defn display-pixel [cube x y z]
  (let [current-layer-value (get-pixel cube x y z)]
    (if (= current-layer-value \2)
      (recur cube x y (inc z))
      current-layer-value)))

;; determine display and write to terminal
;;  use colours and #/. as per 2016 day 8
(defn render [cube]
  (let [display-values (vec
    (for [y (range height)] (conj []
      (for [x (range width)]
        (if (= (display-pixel cube x y 0) \1)
          "\033[31m\033[1m\033[43m#\033[0m" \.)))))]
    (doseq [line display-values]
      (println (map str/join line)))))

;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (str/trim (slurp f))))
(println "Read" (count input) "puzzle characters from one line.")


;;  parse individual layers from input into a vector of vectors
(def layers
  ((fn [s accum]
    (if (str/blank? s)
      accum
      (let [r (subs s 0 pixels_per_layer)]
        (recur (subs s pixels_per_layer) (conj accum r)))))
  input []))

;;  more structure ... data cube: a vector of layers
(def layers-3d 
  (vec
    (for [layer layers]
      ((fn [s accum]
         (if (str/blank? s)
           accum
           (recur (subs s width) (conj accum (subs s 0 width)))))
       layer []))))


;;  apply ruleset and output to screen
(render layers-3d)


