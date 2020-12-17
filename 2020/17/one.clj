
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map vec)
       vec))

;(prn input)


(defn initial-cube [initial-slice num-rounds]
  (let [height (count initial-slice)
        width  (count (initial-slice 0))
        padded-initial-slice
          (->> initial-slice
               (map #(concat % (repeat num-rounds \.)) ,,)
               (map reverse ,,)
               (map #(concat % (repeat num-rounds \.)) ,,)
               (map vec ,,)
               (#(conj % (repeat num-rounds (vec (repeat (+ width (* 2 num-rounds)) \.)))) ,,)
               reverse
               (#(conj % (repeat num-rounds (vec (repeat (+ width (* 2 num-rounds)) \.)))) ,,)
               vec)]
    (-> padded-initial-slice
        (#(conj % (vec (repeat num-rounds
                         (vec (repeat (+ height (* 2 num-rounds))
                                      (vec (repeat (+ width (* 2 num-rounds)) \.))))))))
        reverse
        vec
        (#(conj % (vec (repeat num-rounds
                         (vec (repeat (+ height (* 2 num-rounds))
                                      (vec (repeat (+ width (* 2 num-rounds)) \.))))))))
        vec)))


(println (initial-cube input 6))







