
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-parts
  (-> "puzzle.txt"
      slurp
      (str/split ,, #"\n\n")))

(def input-rules
  (-> (first input-parts)
      (str/split ,, #"\n")))

(def input-messages
  (-> (second input-parts)
      (str/split ,, #"\n")))

;(prn input-rules)
;(prn input-messages)


;;
;;  parse a rule
;;   returns a dictionary of k,v: rule num, vec of vecs of rule nums
;;
(defn parse-rule [s]
  (let [v (vec (str/split s #":\s|\s"))]
    {(Long/parseLong (str/trim (v 0))),            ;;  key is rule num
     (loop [stack (rest v)                         ;;  val is made of the rest
            out   (vector (vector))]
       (cond
         (empty? stack) out
         (= \" (first (str/trim (first stack))))   ;;  the "a" or "b"
           (recur
             (vector)
             (vector (second (first stack))))
         (= \| (first (str/trim (first stack))))   ;;  an `or`
           (recur
             (rest stack)
             (vector out []))
         :else                                     ;;  must be a numeral
           (recur
             (rest stack)
             (assoc out
                    (dec (count out))
                    (conj (out (dec (count out)))
                          (Long/parseLong (str/trim (first stack))))))))}))
         
;(parse-rule (first input-rules))











