
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)))

(doseq [in input]
  (prn in))

(if
  (every? true? (map (fn [line] (some some? (map #(str/index-of line %) ["<>" "[]" "()" "{}"]))) input))
  (println " you should be fine")
  (println " nope: not all of the lines have a simple chunk"))


;;
;;  utility to remove entry from a vector by index
;;
(defn divoc [v n]
  (vec (concat (subvec v 0 n) (subvec v (inc n)))))



(defn find-corruption [s]
  (let [pairs {\} \{, \) \(, \] \[, \> \<}]
    (loop [v (vec s)
           o (into {} (for [end (keys pairs)] (vector end nil)))]
      (prn " working on:" v)
      (let [fs (frequencies v)
            c  (last v)]
        (cond
          (some #{c} (vals pairs))    ;; is the final char an open brace?
          (recur (vec (butlast v)) o)

          (empty? v)
          (into {} (filter #(some? (second %)) o))

          (some #{c} (keys pairs))
          (recur
            (vec (butlast v))
            (if (or
                  (and (fs (pairs c)) (> (fs c) (fs (pairs c))))
                  (not (fs (pairs c))))
              (assoc o c (str/last-index-of (apply str v) c))
              o))

          :else
          (recur (vec (butlast v)) o))))))


          ;(contains? fs (pairs c))
          ;(let [closing-index (str/last-index-of (apply str v) c)
          ;      opening-index (str/last-index-of (apply str v) (pairs c) closing-index)]
          ;  (if (nil? opening-index)
          ;    c
          ;    (recur (-> v
          ;               (divoc ,, closing-index)
          ;               (divoc ,, opening-index)))))


;(prn (for [line input] (find-corruption line)))


(defn parse-chunk [s]
  (let [pairs {\} \{, \) \(, \] \[, \> \<}]
  (loop [v (vec s)
         o {:self nil, :children (list)}]
    (let [c (v 0)
          fs (frequencies v)]
      (cond

        (some (for [end (keys pairs)] (and
                                        (and (contains? fs end) (contains? fs (pairs end)))
                                        true)))
        c

        (empty? (rest v))
        :something

        :else
        (recur nil nil))))))





