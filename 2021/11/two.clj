
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map vec)
       (map (fn [row] (mapv #(Integer/parseInt (str %)) row)) ,,)
       (vec)))


(def nrows (count input))
(def ncols (count (input 0)))


(defn zero-flashes [grid flash-coords]
  (reduce (fn [zeroed-flashes coords]
            (assoc zeroed-flashes
                   (coords 0)
                   (assoc (zeroed-flashes (coords 0)) (coords 1) 0)))
          grid flash-coords))


;;
;;  recursively accepts a grid and an accumulator
;;   the grid is incremented in main; the accumulator
;;   is updated here
;;
(defn substep [grid flash-coords]
  (loop [r 0
         c 0
         g grid
         f flash-coords]
    (cond
      (= r nrows)
      (let [nextg (zero-flashes g f)]       ;; must also be zeroed in main
        (if (some #(< 8 %) (flatten nextg))
          (substep nextg f)
          (vector nextg f)))

      (= c ncols)
      (recur (inc r) 0 g f)

      (< 8 ((g r) c))
      (let [curr ((g r) c)
            update-list
            (cond
              (= r 0)
              (cond
                (= c 0)
                (list
                  (vector (inc r) c)
                  (vector (inc r) (inc c))
                  (vector r (inc c)))

                (= c (dec ncols))
                (list
                  (vector (inc r) c)
                  (vector (inc r) (dec c))
                  (vector r (dec c)))

                :else
                (list
                  (vector (inc r) c)
                  (vector (inc r) (inc c))
                  (vector (inc r) (dec c))
                  (vector r (dec c))
                  (vector r (inc c))))

              (= r (dec nrows))
              (cond
                (= c 0)
                (list
                  (vector (dec r) c)
                  (vector (dec r) (inc c))
                  (vector r (inc c)))

                (= c (dec ncols))
                (list
                  (vector (dec r) c)
                  (vector (dec r) (dec c))
                  (vector r (dec c)))

                :else
                (list
                  (vector (dec r) c)
                  (vector (dec r) (inc c))
                  (vector (dec r) (dec c))
                  (vector r (inc c))
                  (vector r (dec c))))

              (= c 0)
              (list
                (vector (dec r) c)
                (vector (dec r) (inc c))
                (vector (inc r) (inc c))
                (vector r (inc c))
                (vector (inc r) c))

              (= c (dec ncols))
              (list
                (vector (dec r) c)
                (vector (dec r) (dec c))
                (vector (inc r) (dec c))
                (vector r (dec c))
                (vector (inc r) c))

              :else
              (list
                (vector (dec r) c)
                (vector (dec r) (inc c))
                (vector (dec r) (dec c))
                (vector (inc r) (inc c))
                (vector (inc r) (dec c))
                (vector r (dec c))
                (vector r (inc c))
                (vector (inc r) c)))]

        (if (> curr 8)
          (let [new-grid (reduce
                           (fn [gridr pairr]
                             (assoc gridr (pairr 0)
                                    (assoc (gridr (pairr 0))
                                           (pairr 1)
                                           (inc ((gridr (pairr 0)) (pairr 1))))))
                           g
                           update-list)]
            (recur r (inc c)
                   (assoc new-grid r (assoc (new-grid r) c 0))
                   (conj f (vector r c))))
          (recur r (inc c) g f)))

      :else
      (recur r (inc c) g f))))



;;
;;  main
;;
(loop [i 1
       grid input
       accum 0]
  (if (= i 100001)    ;; something big
    (println accum)   ;; won't get here
    (let [[g f] (substep grid (vector))
          nextg (zero-flashes
                  (reduce (fn [gridr rowr] (conj gridr (mapv inc rowr))) [] g)
                  f)]
      ;(println " after step" i)
      ;(doseq [row nextg] (println row))
      ;(println)
      (if (= 0 (count (remove zero? (flatten nextg))))
        (println i)
        (recur (inc i) nextg (+ accum (count f)))))))



