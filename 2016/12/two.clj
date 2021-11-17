
(require '[clojure.string :as str])

(def program (->> "puzzle.txt"
                  slurp
                  (#(str/split % #"\n") ,,)
                  (map #(str/split % #"\s") ,,)
                  (map #(map
                          (fn [e]
                            (let [rev (re-matches #"(-?\d+)" e)]
                              (if rev (Integer/parseInt (rev 1)) e))) %) ,,)
                  (map vec ,,)
                  vec))



(defn operate [d op]
  ;(println " processing op:" op "with registers:" d)
  (case (op 0)
    "cpy" (assoc d
                 (op 2) (if (integer? (op 1))
                          (op 1)
                          (d (op 1)))
                 :ip (inc (d :ip)))
    "inc" (assoc d
                 (op 1) (inc (d (op 1)))
                 :ip (inc (d :ip)))
    "dec" (assoc d
                 (op 1) (dec (d (op 1)))
                 :ip (inc (d :ip)))
    "jnz" (let [x (if (integer? (op 1)) (op 1) (d (op 1)))]
            (assoc d
                 :ip (+ (d :ip) (if (zero? x) 1 (op 2)))))))



(loop [d    {"a" 0, "b" 0, "c" 1, "d" 0, :ip 0}]
  (if (>= (d :ip) (count program))
    (println d)
    (recur (operate d (program (d :ip))))))



