
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       vec
       (mapv #(Long/parseLong (str %)) ,,)))

(prn input)


(let [nmoves 10]
  (loop [circle  input
         current (first input)
         moves   0]
    (println " circle:" circle)
    (let [ci        (.indexOf circle current)
          wrap      (vec (concat circle circle))
          pickup    (subvec wrap (inc ci) (+ ci 4))
          leave     (vec (concat
                      (vector (first circle))
                      (subvec wrap (+ ci 4) (+ (+ ci 4) (- (count circle) 4)))))
          di        (loop [stack leave
                           d     (if (= 0 (dec current)) (count input) (dec current))]
                      (let [found (.indexOf stack d)]
                        (if (= -1 found)
                          (recur (rest stack) (if (= 0 (dec d)) (count input) (dec d)))
                          found)))
          new-circ  (vec (concat
                      (subvec leave 0 (+ 1 di))
                      pickup
                      (subvec leave (inc di))))
          new-curr  (new-circ (mod (inc (.indexOf new-circ current)) (count new-circ)))]
      (if (= moves nmoves)
        (let [final-wrap (vec (concat new-circ new-circ))
              i1  (.indexOf final-wrap 1)
              out (apply str (subvec final-wrap (inc i1) (+ i1 (count new-circ))))]
          (prn out))
        (recur new-circ new-curr (inc moves))))))


