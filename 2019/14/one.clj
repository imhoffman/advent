(require '[clojure.string :as str])


;;  unpack reagents from comma-separated string
(defn uncomma [string-of-chemicals]
  (list (str/split (str/replace string-of-chemicals \, \space) #"\s+")))

;;  dictionary of reagents
;;   key = chemical; value = amount
(defn make-inputs-dict [ins accum]
  (if (empty? ins)
    accum
    (recur (rest (rest ins)) (assoc accum (second ins) (first ins)))))


;;  dictionary of dictionaries of reaction inputs and outputs
;;   key = {product amt}; value = { {reactant amt} }
(defn parser [rxns dict]
  (for [chems 
        (for [rxn rxns]
          (let [j    (dec (str/index-of rxn \=))
                ins  (str/trim (subs rxn 0 j))
                outs (str/trim (subs rxn (+ j 4)))]
            (list ins outs)))]
    (let [ws-ins-list (uncomma (first chems))
          ws-outs-list (str/split (second chems) #"\s+")]
      (assoc dict
             {(second ws-outs-list) (first ws-outs-list)}
             (make-inputs-dict (first ws-ins-list) {})))))



;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
(println "Read" (count input) "lines.")


(println (parser input {}))


