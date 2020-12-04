
(require '[clojure.string :as str])

(defn parser [r]
  (let [groups (re-seq #"((\w+):([#|\w]+))\s*" r)     ;;  need to match beyond '#' !!!
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
  (if (reduce #(and %1 %2) (for [f mandatory-fields] (contains? record-dict f)))
    (let [byr (Long/parseLong (record-dict "byr"))
          iyr (Long/parseLong (record-dict "iyr"))
          eyr (Long/parseLong (record-dict "eyr"))
          hgt (record-dict "hgt")
          hcl (record-dict "hcl")
          ecl (record-dict "ecl")
          pid (record-dict "pid")]
      (and
        (and (>= byr 1920) (<= byr 2002))
        (and (>= iyr 2010) (<= iyr 2020))
        (and (>= eyr 2020) (<= eyr 2030))
        (let [[_ ns u] (re-matches #"(\d+)(\w+)" hgt)
              n (Long/parseLong ns)]
          (if (= u "cm")
            (and (>= n 150) (<= n 192))
            (and (>= n 59)  (<= n 76))))
        (re-matches #"#[0-9|a-f]{6}" hcl)
        (some #(= ecl %) ["amb" "blu" "brn" "gry" "grn" "hzl" "oth"])
        (re-matches #"[0-9]{9}" pid)
        true))
    false))


(let [batch-dicts (map parser batch)]
  (println
    (count (filter true? (for [d batch-dicts] (valid? d))))))

;(doseq [b batch] (println b "\n" (parser b) "\n"))

