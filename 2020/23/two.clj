
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-short
  (->> "puzzle.txt"
       slurp
       str/trim
       vec
       (mapv #(Long/parseLong (str %)) ,,)))

(prn input-short)

(def input 
  (vec (concat input-short (range (inc (count input-short)) 1000001))))



(let [nmoves 10000000]
  (loop [circle  input
         current (first input)
         moves   0]
    (let [ci        (.indexOf circle current)
          wrap      (vec (concat circle circle))
          pickup    (subvec wrap (+ 1 ci) (+ ci 4))
          leave     (vec (concat
                      (subvec circle ci (+ 1 ci))
                      (subvec wrap
                              (+ ci 4)
                              (+ ci 4 (- (count circle) 4)))))
          [dest di] (loop [stack leave
                           d     (if (= 0 (dec current)) (count input) (dec current))]
                      (let [found (.indexOf stack d)]
                        (if (= -1 found)
                          (recur (rest stack) (if (= 0 (dec d)) (count input) (dec d)))
                          (vector d (.indexOf leave d)))))
          new-circ  (vec (concat
                      (subvec leave 0 (+ 1 di))
                      pickup
                      (subvec leave (+ 1 di))))
          new-curr  (new-circ (mod (inc (.indexOf new-circ current)) (count new-circ)))]
      ;(println "\n  current:" current)
      ;(println "   circle:" circle)
      ;(println "   pickup:" pickup)
      ;(println "    leave:" leave)
      ;(println "     dest:" dest)
      ;(println " put down:" new-circ)
      ;(println " next cur:" new-curr)
      (if (= moves (dec nmoves))
        (let [final-wrap (vec (concat new-circ new-circ))
              i1  (.indexOf final-wrap 1)
              out (* (final-wrap (+ 1 i1)) (final-wrap (+ 2 i1)))]
          (prn out))
        (recur new-circ new-curr (inc moves))))))


