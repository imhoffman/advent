
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



(defn operate [d]
  ;(println " ip:" (d :ip) "  registers:" (d "a") (d "b") (d "c") (d "d"))
  ;(doseq [i (range (count (d :ram)))] (println i ((d :ram) i)))
  (letfn [(de-ref [x] (if (integer? x) x (d x)))]
    (let [op ((d :ram) (d :ip))]
      (case (op 0)
        "cpy" (if (integer? (op 2))           ;; if tgl broke it
                (assoc d :ip (inc (d :ip)))   ;; then skip it
                (assoc d
                       (op 2) (de-ref (op 1))
                       :ip (inc (d :ip))))
        "inc" (if (integer? (op 1))           ;; tgl might become inc
                (assoc d :ip (inc (d :ip)))   ;; skip it
                (assoc d
                       (op 1) (inc (d (op 1)))
                       :ip (inc (d :ip))))
        "dec" (if (integer? (op 1))           ;; tgl->inc->dec could happen
                (assoc d :ip (inc (d :ip)))   ;; skip it
                (assoc d
                       (op 1) (dec (d (op 1)))
                       :ip (inc (d :ip))))
        "jnz" (let [arg1 (de-ref (op 1))]
                (assoc d :ip (+ (d :ip) (if (zero? arg1) 1 (de-ref (op 2))))))
        "tgl" (let [arg1 (+ (d :ip) (de-ref (op 1)))]
                ;(println " toggling op" arg1 ((d :ram) arg1))
                (if (or (>= arg1 (count (d :ram))) (< arg1 0))
                  (assoc d :ip (inc (d :ip)))              ;; "nothing happens"
                  (let [tgl-op ((d :ram) arg1)]            ;; else
                    (case (count tgl-op)
                      3 (case (tgl-op 0)
                          "jnz" (assoc d
                                       :ram                ;; write to ram
                                       (assoc (d :ram)     ;; the ram entry
                                              arg1         ;; at vec index target
                                              (assoc
                                                ((d :ram) arg1)
                                                0 "cpy"))
                                       :ip (inc (d :ip)))
                          (assoc d              ;; default case
                                 :ram
                                 (assoc (d :ram)
                                        arg1
                                        (assoc
                                          ((d :ram) arg1)
                                          0 "jnz"))
                                 :ip (inc (d :ip))))
                      2 (case (tgl-op 0)
                          "inc" (assoc d
                                       :ram                ;; write to ram
                                       (assoc (d :ram)     ;; the ram entry
                                              arg1         ;; at vec index targeta
                                              (assoc
                                                ((d :ram) arg1)
                                                0 "dec"))
                                       :ip (inc (d :ip)))
                          (assoc d :ram              ;; default case
                                 (assoc (d :ram)
                                        arg1
                                        (assoc
                                          ((d :ram) arg1)
                                          0 "inc"))
                                 :ip (inc (d :ip))))
                      (println " 3/2 case failed")))))
        (println " op 0 case failed")))))




(loop [d {"a" reg-a, "b" 0, "c" 0, "d" 0, :ip 0, :ram program}]
  (if (>= (d :ip) (count (d :ram)))
    (println " register a at halt:" (d "a"))
    (recur (operate d))))


;;  7 in reg a yields 6*5*5*70

