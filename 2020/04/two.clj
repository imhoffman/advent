
(require '[clojure.string :as str])

(defn parser [r]
  (let [groups (re-seq #"((\w+):([#|\w]+))\s*" r)
        dict   (into {} (for [e groups] (vector (e 2) (e 3))))]
        ;dict   (into {} (for [e groups] (vector (e 2) (str/trim (e 3)))))]
    dict))

(def batch
  ((fn [s] (-> s
               slurp
               (str/split #"(\n|\r\n)\1")))
   "puzzle.txt"))


(def mandatory-fields
  ["byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"])


(defn valid? [record-dict]
  (println "\n considering:" record-dict)
  (if (reduce #(and %1 %2) (for [f mandatory-fields] (contains? record-dict f)))
    (let [byr (Long/parseLong (record-dict "byr"))
          iyr (Long/parseLong (record-dict "iyr"))
          eyr (Long/parseLong (record-dict "eyr"))
          hgt (record-dict "hgt")
          hcl (record-dict "hcl")
          ecl (record-dict "ecl")
          pid (record-dict "pid")]
      (do (println byr iyr eyr hgt hcl ecl pid)
      (and
        (and (>= byr 1920) (<= byr 2002))
        (and (>= iyr 2010) (<= iyr 2020))
        (and (>= eyr 2020) (<= eyr 2030))
        (let [matches (re-matches #"(\d{2,3})([cmin]{2})" hgt)]
          (when matches
            (let [[_ ns u] matches
                  n (Long/parseLong ns)]
              (println " hgt matches:" ns u)
              (cond
                (= u "cm") (and (>= n 150) (<= n 193))
                (= u "in") (and (>= n 59)  (<= n 76))
                :else false))))
        (re-matches #"#[0-9|a-f]{6}" hcl)
        (some #(= ecl %) ["amb" "blu" "brn" "gry" "grn" "hzl" "oth"])
        (re-matches #"[0-9]{9}" pid)
        true)))  ;;  so that I can filter on true
    false))


(let [batch-dicts (map parser batch)]
;  (println
;    (count (filter true? (for [d batch-dicts] (valid? d))))))
  (println "\n total count:"
  (loop [stack batch-dicts
         c     0]
    (if (empty? stack)
      c
      (if (valid? (first stack))
        (do (println " PASS:" (first stack))
            (recur (rest stack) (inc c)))
        (do (println " FAIL:" (first stack))
            (recur (rest stack) c)))))
  )
  )


;(doseq [b batch] (println b "\n" (parser b) "\n"))

;; 118 too low
;; 119 after `trim`ming and adding trailing `.*` to regexs
;; 122 after fixing cm height typo
;; 121 correct: after removing `trim`ming ...

