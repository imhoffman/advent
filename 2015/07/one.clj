
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(defn to-2r [n]
  (->> n
      (#(Integer/toString % 2) ,,)
      (Integer/parseInt ,,)
      (format "%08d" ,,)
      (list "2r" ,,)
      str/join))



;;
;;  make sense of the regex groups in a single input line; parsing
;;   any literals alongs with the op, etc.
;;   return the dict-of-dicts entry
;;   for NOT, enter the argument as an input
;;
(defn parser [rev]
  (let [v  (filterv (complement empty?) (rest rev))
        pd (cond
             ;;  NOT is the only leading op
             (= (v 0) "NOT")
             {:inputs (vector (v 1)), :op (v 0), :output (v 2)}

             ;;  assignments, literal or symbolic
             (= 2 (count v))
             {:inputs (vector (v 0)), :op nil, :output (v 1)}

             ;;  normal `val OP val` structure
             :else
             {:inputs (vector (v 0) (v 2)), :op (v 1), :output (v 3)})]
    ;;  search for and parse any numeral strings
    ;;   which can only appear in the inputs
    (assoc pd :inputs
           (mapv #(if (re-matches #"\d+" %) (Integer/parseInt %) %) (pd :inputs)))))


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"([0-9a-z]*?)\s*([A-Z]*)\s*([0-9a-z]+) -> ([a-z]+)" %) ,,)
       (map parser ,,)))


(defn op [inputs operation]
  (case operation
    "NOT"
    (bit-not (inputs 0))

    "AND"
    (bit-and (inputs 0) (inputs 1))

    "OR"
    (bit-or (inputs 0) (inputs 1))

    "RSHIFT"
    (bit-shift-right (inputs 0) (inputs 1))

    "LSHIFT"
    (bit-shift-left (inputs 0) (inputs 1))

    nil
    (inputs 0)

    :fail))



;;
;;  main
;;

(let [outputs (reduce
                (fn [a b]
                  (let [ins (b :inputs)]
                    (assoc
                      a
                      (b :output)
                      (if (every? number? ins)
                        (op ins (b :op))
                        nil))))
                {} input)]
  (loop [d outputs]
    (let [known (reduce
                  (fn [a b]
                    (if (every?
                          #(or (% 0) (number? (% 1)))
                          ;#(or (not (% 0)) (number? (% 1)))
                          (map vector (map d (b :inputs)) (b :inputs)))
                      (conj a b) a))
                  (list) input)]
      (if (every? (complement nil?) (vals d))
        (println (d "a"))

        (recur
          (reduce
            (fn [a b]
              (assoc
                a (b :output)
                ;(op (mapv d (b :inputs))
                (op (mapv #(or (% 0) (% 1)) (map vector (map d (b :inputs)) (b :inputs)))
                    (b :op))))
            d known))))))

