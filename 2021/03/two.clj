
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       vec))


(println
  (*
   (loop [in input
          index 0]
     (if (= 1 (count in))
       (Integer/parseInt (apply str (first in)) 2)
       (let [freqs (frequencies (for [c (range (count in))] ((vec (in c)) index)))
             least (cond
                     (false? (contains? freqs \0)) \1
                     (false? (contains? freqs \1)) \0
                     :else (if (> (freqs \0) (freqs \1)) \1 \0))]
         ;(println " index:" index "  in:" in )
         (recur
           (filterv #(= least (nth % index)) in)
           (inc index)))))
   (loop [in input
          index 0]
     (if (= 1 (count in))
       (Integer/parseInt (apply str (first in)) 2)
       (let [freqs (frequencies (for [c (range (count in))] ((vec (in c)) index)))
             most  (cond
                     (false? (contains? freqs \0)) \1
                     (false? (contains? freqs \1)) \0
                     :else (if (> (freqs \0) (freqs \1)) \0 \1))]
         ;(println " index:" index "  in:" in )
         (recur
           (filterv #(= most  (nth % index)) in)
           (inc index)))))))



