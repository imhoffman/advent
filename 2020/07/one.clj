
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(re-matches #"(\w+ \w+) \w+ \w+ (.*)$" %))
       (map #(hash-map (nth % 1) (nth % 2)))))

(defn parse-val-string [val-string]
  (loop [stack (str/split val-string #"\,")
         out   []]
    (if (empty? stack)
      (apply hash-map (flatten out))
      (recur
        (rest stack)
        (if (= "no" (second (re-matches #"\s*(\w+).*$" (first stack))))
          (conj out (vector :none 0))
          (conj out (vector
                      (second (re-matches #"\s*\d+ (\w+ \w+).*$" (first stack)))
                      (Long/parseLong (second (re-matches #"\s*(\d+).*$" (first stack)))))))))))


(def input-dict
  (loop [ds  input
         out {}]
    (if (empty? ds)
      out
      (recur
        (rest ds)
        (assoc out
               (first (keys (first ds)))
               (parse-val-string (first (vals (first ds)))))))))

;(prn input-dict)


(defn counter [key-color]
  (let [k key-color
        d input-dict                  ;;  defined above
        daughter-colors (keys (d k))]
    ;(println " working on" k "with daughters" daughter-colors)
    (cond
      (= :none (first daughter-colors))
        0
      (contains? (set daughter-colors) "shiny gold")
        1
      :else
        (if (< 0 (apply + (for [c daughter-colors] (counter c)))) 1 0))))
          

(println " number of colors that eventually contain a gold bag:"
         (apply + (for [k (keys input-dict)] (counter k))))
            
;; 109 too high

