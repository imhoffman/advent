
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
;;  use existing dict of k,v as system of k mod v (???)
;;   I must be supplying the system incorrectly ... the algo works 
;;
;;  sources:
;;   https://www.youtube.com/watch?v=2-tdwLqyaKo
;;   https://en.wikipedia.org/wiki/Modular_multiplicative_inverse#Applications
;;
;;  works for this:
;;   https://youtu.be/zIFehsBHB8o?t=719 
;;
(defn crt [dict]
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
          (for [pair (map (partial take 2) (combo/permutations (vals dict)))]
            (gcd (first pair) (second pair))))
      (println " Applying CRT.")
      (do (println " CRT does not apply.\n") (System/exit 1)))
    ;;  compute the answer
    (let [final-mod (apply * (vals dict))
          sys (for [kv dict]
                (*
                 (* (key kv) (apply * (vals (dissoc dict (key kv)))))
                 (naive-mmi (apply * (vals (dissoc dict (key kv)))) (val kv))))]
      (mod (apply + sys) final-mod))))


(prn (crt notes-dict))


;;  expose for testing ...
(defn naive-mmi [a m]
  (letfn [(gcd [a b] (long (.gcd (biginteger a) (biginteger b))))]
    (if (not= 1 (gcd a m))
      (prn " failed mmi condition")
      (loop [x 0]
        (cond
          (= 1 (mod (* a x) m)) x
          (= x m) (prn " failed mmi search")
          :else (recur (inc x)))))))


