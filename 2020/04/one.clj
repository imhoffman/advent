
(require '[clojure.string :as str])

(defn parser [r]
  (let [groups (re-seq #"((\w+):(#|\w+))\s*" r)     ;;  need to match beyond '#' !!!
        dict   (into {} (for [e groups] (vector (e 2) (e 3))))]
    dict))

(def batch
  ((fn [s] (-> s
               slurp
               (str/split #"(\n|\r\n)\1")))
   "puzzle.txt"))


(def mandatory-fields
  ["byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"])


(defn valid? [record-dict]
  (reduce #(and %1 %2) (for [f mandatory-fields] (contains? record-dict f))))


(let [batch-dicts (map parser batch)]
  (println
    (count (filter true? (for [d batch-dicts] (valid? d))))))


