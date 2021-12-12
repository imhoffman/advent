
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


(defn score [line]
  (loop [s (reverse line)
         a 0]
    (if (empty? s)
      a
      (recur (rest s)
             (+ (* 5 a)
                (cond
                  (= \( (first s)) 1
                  (= \[ (first s)) 2
                  (= \{ (first s)) 3
                  (= \< (first s)) 4))))))


(defn completion-score [line]
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
          (if ibad
            nil   ;; filter corrupt entires later
            (score s)))))))



(let [good-scores (sort (filter some? (for [line input] (completion-score line))))]
  (prn (nth good-scores (quot (count good-scores) 2))))


