(require '[clojure.string :as str])

;;  unpack reagents from comma-separated string
(defn uncomma [string-of-chemicals]
  (list (str/split (str/replace string-of-chemicals \, \space) #"\s+")))

;;  dictionary of reagents
;;  (are the inputs to a reaction called the reagents? reactants?)
;;   key = chemical; value = amount
(defn make-inputs-dict [ins accum]
  (if (empty? ins)
    accum
    (recur (rest (rest ins)) (assoc accum (second ins) (first ins)))))


;;  dictionary of dictionaries of reaction inputs and outputs
;;   key = product; value = (amt of product, {reactant,amt})
(defn parser [rxns dict] (into (hash-map)
  (for [chems (for [rxn rxns]
                (let [j    (dec (str/index-of rxn \=))
                      ins  (str/trim (subs rxn 0 j))
                      outs (str/trim (subs rxn (+ j 4)))]
                  (list ins outs)))]
    (let [whitesplit-ins-list (uncomma (first chems))
          whitesplit-outs-list (str/split (second chems) #"\s+")]
      (assoc dict
             (second whitesplit-outs-list)
             (list (first whitesplit-outs-list)
                   (make-inputs-dict (first whitesplit-ins-list) {})))))))


;;  recursively walk the tree, tallying costs
;;   each reactant dictionary in the val of each rxn-dict entry
;;   will provide a work stack;
;;  initially, the traversal stack is equal to (get rxn-dict product)
(defn find-cost [rxn-dict product accum]
  (let [inputs-dict (second (get rxn-dict) product)
        inputs      (keys inputs-dict)]    ;; inputs aka reagents
    (if (get inputs-dict "ORE")
      (* accum (first (get inputs-dict "ORE")))  ;; scale by coeff
      (let [work-stack (keys inputs-dict)
            job (peek work-stack)]
        (recur rxn-dict job   ;; does job go here?
               ((fn [reagents cost]   ;; this lambda is for the accum in the recur
                  (if (empty? reagents)
                    cost ; ... hmmm






;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
(println "Read" (count input) "lines.")


(let [dict (parser input {})]
  (println (get dict "FUEL")))

