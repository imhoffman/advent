
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


(let [d input-dict]                  ;;  defined above
  (defn counter [key-color]
    (let [k key-color
          daughter-colors (keys (d k))]
      ;(println " working on" k "with daughters" daughter-colors)
      (loop [stack daughter-colors
             accum 0]
        (if (or (empty? stack) (= :none (first stack)))
          accum
          (let [c (first stack)]
            (recur
              (rest stack)
              (+ accum ((d k) c) (* ((d k) c) (counter c))))))))))
          

(println " number of bags contained by a single gold bag:" (counter "shiny gold"))
            

