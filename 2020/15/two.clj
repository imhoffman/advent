
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-as-vec
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #","))
       (map #(Long/parseLong %))
       vec))


(def input-as-dict
  (apply hash-map
         (flatten
           (for [i (range (count input-as-vec))]
             (list
               (input-as-vec i)
               (inc i))))))


(prn input-as-vec)
;(prn input-as-dict)


(loop [just-said (last input-as-vec)
       newd      input-as-dict
       oldd      (dissoc newd just-said)
       turn      (inc (count newd))]          ;;  1-indexed
  ;(println " turn:" turn "  just-said:" just-said "  newd:" newd)
  ;(if (= turn (inc 2020))
  (if (= turn (inc 30000000))
    (prn just-said)
    (let [prev (oldd just-said)         ;;  nil if key not present
          say  (if prev (- (dec turn) prev) 0)]
      (recur
        say
        (assoc newd say turn)
        newd
        (inc turn)))))







