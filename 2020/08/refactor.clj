
(require '[clojure.string :as str])

;;
;;  read in text file and package
;;   as a vector of two-element vectors
;;    e.g. ["jmp" -34]
;;
(def orig-program
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(str/split % #" "))
       (map #(vector (first %) (Long/parseLong (second %))))
       vec))

;;
;;  input: (vector ram op arg pc accum)
;;    e.g. (vector prg "jmp" -34 455 879)
;;  output (vector new-ram new-pc new-accum)
;;
(defn exec [arg-vec]
  (let [[ram op arg pc accum] arg-vec
        ops {"jmp" (vector ram (+ pc arg) accum)
             "acc" (vector ram (+ pc 1)   (+ accum arg))
             "nop" (vector ram (+ pc 1)   accum)}]
    (ops op)))


;;
;;  loop through program and execute
;;   successive operations until halt
;;
(defn execute [prg]
  (loop [ram   prg
         pc    0
         accum 0]
    (if (>= pc (count ram))      ;;  the halt condition
      accum                      ;;  the return upon halt
      (let [op  ((ram pc) 0)
            arg ((ram pc) 1)
            [new-ram new-pc new-accum] (exec (vector ram op arg pc accum))]
        (recur new-ram new-pc new-accum)))))


(println "\n part one:")

(defn part-one [prg]
  (loop [ram   prg
         pc    0
         accum 0
         pcs   #{}]              ;;  assuming 0 isn't the revisted address
    (if (contains? pcs pc)       ;;  the halt condition
      accum                      ;;  the return upon halt
      (let [op  ((ram pc) 0)
            arg ((ram pc) 1)
            [new-ram new-pc new-accum] (exec (vector ram op arg pc accum))]
        (recur new-ram new-pc new-accum (conj pcs pc))))))

(println " accumulator when an address is first revisited:" (part-one orig-program))


(println "\n part two:")

(defn try-prg [ram]
  (let [try-limit (* 3 (count ram))]
  (loop [pc    0
         accum 0
         execs 0]
    (cond
      (>= pc (count ram)) accum
      (= execs try-limit) :fail
      :else
        (let [op  ((ram pc) 0)
              arg ((ram pc) 1)]
          (recur
            (if (= op "jmp")
              (+ pc arg)
              (inc pc))
            (if (= op "acc")
              (+ accum arg)
              accum)
            (inc execs)))))))


(let [p orig-program]
  (loop [last-index 0]
    (let [[swap-index,op] (loop [i last-index]
                            (if (or (= ((p i) 0) "nop")
                                    (= ((p i) 0) "jmp"))
                              (vector i ((p i) 0))
                              (recur (inc i))))
          ram (-> (p swap-index)
                  (assoc ,, 0 (if (= op "nop") "jmp" "nop"))
                  (#(assoc p swap-index %)))]
      ;(println " swapping out a" op "at index" swap-index)
      (let [result (try-prg ram)]
        (if (= :fail result)
          (recur (inc last-index))
          (println " accumulator when the altered program halts:" result
                   "\n                    which should agree with:" (execute ram)
                   "\n"))))))


