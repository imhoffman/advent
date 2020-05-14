
(require '[clojure.string :as str])


(def list-of-dimension-lists
  ((fn [filename]
     (->> filename
          slurp
          (re-seq #"\w+")
          (map #(str/split % #"[x]"))
          (map #(map (fn [s] (Long/parseLong s)) %))))
   "puzzle.txt"))


(defn wrapping-paper-area-needed [list-of-three-dimensions]
  (let [length (nth list-of-three-dimensions 0)
        width  (nth list-of-three-dimensions 1)
        height (nth list-of-three-dimensions 2)]
    (+ (* 2 length width) (* 2 length height) (* 2 height width)
       (min
            (* length width)
            (* height width)
            (* length height)))))


(println " the answer to part one is:"
         (apply + (for [present list-of-dimension-lists]
                    (wrapping-paper-area-needed present))))



