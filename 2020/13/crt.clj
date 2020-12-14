
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.math.combinatorics :as combo])


(def notes-vec
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       second
       (#(str/split % #","))
       (map #(re-matches #"\d+" %))
       (map #(if (nil? %) nil (Long/parseLong %)))
       vec))

(prn notes-vec)


(def notes-dict
  (loop [s notes-vec
         i 0
         out {}]
    (if (empty? s)
      out
      (recur (rest s)
             (inc i)
             (if (nil? (first s))
               out
               (assoc out i (first s)))))))


(prn notes-dict)


;;
;;  Chinese Remainder Theorem
;;
;;  input list of a,m pairs as system of a mod m
;;
;;  sources:
;;   https://www.youtube.com/watch?v=2-tdwLqyaKo
;;   https://en.wikipedia.org/wiki/Modular_multiplicative_inverse#Applications
;;
;;  works for this:
;;   https://youtu.be/zIFehsBHB8o?t=719 
;;  and for this (the final line is wrong):
;;   http://homepages.math.uic.edu/~leon/mcs425-s08/handouts/chinese_remainder.pdf
;;
(defn crt [inp]
  (letfn [(gcd [a b] (long (.gcd (biginteger a) (biginteger b))))
          (naive-mmi [a m] (if (not= 1 (gcd a m))
                             (prn " failed mmi condition")
                             (loop [x 0]
                               (cond
                                 (= 1 (mod (* a x) m)) x
                                 (= x m) (prn " failed mmi search")
                                 :else (recur (inc x))))))]
    ;;  ensure coprime mods
    (if (every?
          #(= 1 %)
          (for [pair (map (partial take 2) (combo/permutations (map second inp)))]
            (gcd (first pair) (second pair))))
      (println " Applying CRT.")
      (do (println " CRT does not apply.\n") (System/exit 1)))
    ;;  compute the answer
    (let [ms  (map second inp)
          m   (apply * ms)
          as  (map first inp)
          zs  (for [p inp] (/ m (second p)))
          ys  (for [p inp] (naive-mmi (/ m (second p)) (second p)))
          ws  (for [pair (partition 2 (interleave ys zs))]
                (mod (* (first pair) (second pair)) m))
          xs  (for [pair (partition 2 (interleave as ws))]
                (* (first pair) (second pair)))]
                ;(mod (* (first pair) (second pair)) m))]
      (println "  m:"  m)
      (println " ms:" ms)
      (println " as:" as)
      (println " zs:" zs)
      (println " ys:" ys)
      (println " ws:" ws)
      (println " xs:" xs)
      (println "  x:" (apply + xs))
      (mod (apply + xs) m))))


;;
;;  CRT algo above is working; how to massage the
;;   puzzle input to use it ?
;;
;;   t + mins = 0 mod id
;;      t = (m-a) mod m
;;
;;    so, must feed k,v = (m-a),m to CRT !   woohoo!
;;
(def crt-input
  (for [kv notes-dict] (vector (- (val kv) (key kv)) (val kv))))


(prn (crt crt-input))

;; 205146236914224 too low
;; 426411251956034 too high

