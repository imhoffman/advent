
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(re-matches #"([A-Z])(\d+).*$" %))
       (map rest)
       (map vec)
       (map #(assoc % 0 (first (first %))))
       (map #(assoc % 1 (Long/parseLong (second %))))))

;(prn input)


;; N 0, E 1, S 2, W 3
(loop [stack input
       wayx  10
       wayy  1
       lat   0
       lon   0]
  (if (empty? stack)
    (prn (+ (Math/abs lat) (Math/abs lon)))
    (let [[instr x] (first stack)]
      (cond
        (= \R instr)
          (let [nphi (quot x 90)]
            (cond
              (= 0 nphi) (recur (rest stack) wayx wayy lat lon)
              (= 1 nphi) (recur (rest stack) wayy (- wayx) lat lon)
              (= 2 nphi) (recur (rest stack) (- wayx) (- wayy) lat lon)
              (= 3 nphi) (recur (rest stack) (- wayy) wayx lat lon)
              :else (prn " fail: greater than 270deg cw rot")))
        (= \L instr)
          (let [nphi (quot x 90)]
            (cond
              (= 0 nphi) (recur (rest stack) wayx wayy lat lon)
              (= 1 nphi) (recur (rest stack) (- wayy) wayx lat lon)
              (= 2 nphi) (recur (rest stack) (- wayx) (- wayy) lat lon)
              (= 3 nphi) (recur (rest stack) wayy (- wayx) lat lon)
              :else (prn " fail: greater than 270deg ccw rot")))
        (or (= \N instr) (= \S instr))
          (recur (rest stack)
                 wayx
                 (if (= \N instr) (+ wayy x) (- wayy x))
                 lat
                 lon)
        (or (= \E instr) (= \W instr))
          (recur (rest stack)
                 (if (= \E instr) (+ wayx x) (- wayx x))
                 wayy
                 lat
                 lon)
        (= \F instr)
          (recur (rest stack)
                 wayx
                 wayy
                 (+ lat (* x wayx))
                 (+ lon (* x wayy)))
        :else (prn " fail: unknown instruction")))))
                 
               
;; 99513


