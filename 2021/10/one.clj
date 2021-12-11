
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)))

;(doseq [in input] (prn in))

(if
  (every? true? (map (fn [line] (some some? (map #(str/index-of line %) ["<>" "[]" "()" "{}"]))) input))
  (println " you should be fine")
  (println " nope: not all of the lines have a simple chunk"))


;;
;;  utility to remove entry from a vector by index
;;
(defn divoc [v n]
  (vec (concat (subvec v 0 n) (subvec v (inc n)))))


(defn find-corruption [line]
  (loop [s line]
    (let [ipair (first (filter some? (map #(str/index-of s %) ["<>" "[]" "()" "{}"])))]
      (cond
        (empty? s)
        0

        ipair
        (recur (-> s, vec, (divoc ,, ipair), (divoc ,, ipair), (#(apply str %) ,,)))

        :else
        (let [bads (filter some? (map #(str/index-of s %) [">" "]" ")" "}"]))
              ibad (if (empty? bads) nil (apply min bads))]
          (cond
            (nil? ibad) 0
            (= \) ((vec s) ibad)) 3
            (= \] ((vec s) ibad)) 57
            (= \} ((vec s) ibad)) 1197
            (= \> ((vec s) ibad)) 25137
            :else 0))))))



(prn (apply + (for [line input] (find-corruption line))))


