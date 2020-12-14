
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (-> "puzzle.txt"
      slurp
      (str/split #"\n")))

;(prn input)


(defn pow [b e]
  (loop [c e
         a 1]
    (if (= 0 c)
      a
      (recur (dec c) (* a b)))))


(defn dec-to-bit-dict [decimal]
  (loop [d decimal
         exp 35
         out {}]
    (if (= 0 d)
      out
      (recur
        (mod d (pow 2 exp))
        (dec exp)
        (assoc out exp (if (not= (mod d (pow 2 exp)) d) 1 0))))))


(defn bit-dict-to-dec [bit-dict]
  (apply +
         (for [kv bit-dict] (if (= 1 (val kv)) (pow 2 (key kv)) 0))))


;;  accepts a string of 0/1/X
(defn mask-to-bit-dict [mask]
  (loop [m (reverse mask)
         b 0
         out {}]
    (if (empty? m)
      out
      (recur
        (rest m)
        (inc b)
        (assoc out b (first m))))))


;;
;;  accept decimal values, apply mask, return decimal value
;;
(defn assign-value [mask memory-value incoming-value]
  )


(defn run-program [program-strings]
  (loop [prg  program-strings
         mask nil
         mem  {}]
    (if (empty? prg)
      mem
      (let [instr (first prg)
            cmd   (first (str/split instr #" "))]
      (recur
        (rest prg)
        (if (= cmd "mask")
          (mask-to-bit-dict (second (re-matches #"\w+ = (\w+).*$" instr)))
          mask)
        (if (= cmd "mask")
          mem
          (let [[_ a v] (re-matches #"^.{4}(\d+).{4}(\d+).*$" instr)
                addr  (Long/parseLong a)
                value (Long/parseLong v)]
            (assoc mem
                   addr
                   (assign-value mask (if (mem addr) (mem addr) 0) value)))))))))







