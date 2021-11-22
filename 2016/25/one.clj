
(require '[clojure.string :as str])

(def reg-a (Integer/parseInt (first *command-line-args*)))

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
  (letfn [(de-ref [x] (if (integer? x) x (d x)))]
    (case (op 0)
      "out" (do
              (println (de-ref (op 1)))
              (assoc d :ip (inc (d :ip))))
      "cpy" (assoc d
                   (op 2) (de-ref (op 1))
                   :ip (inc (d :ip)))
      "inc" (assoc d
                   (op 1) (inc (d (op 1)))
                   :ip (inc (d :ip)))
      "dec" (assoc d
                   (op 1) (dec (d (op 1)))
                   :ip (inc (d :ip)))
      "jnz" (assoc d
                   :ip (+ (d :ip) (if (zero? (de-ref (op 1))) 1 (op 2)))))))


(loop [d {"a" reg-a, "b" 0, "c" 0, "d" 0, :ip 0}]
  (if (>= (d :ip) (count program))
    (println d)
    (recur (operate d (program (d :ip))))))



