
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

;;  The ship starts by facing east. Only the L and R actions change the direction the ship is facing.

(loop [stack input
       direc 1        ;; N 0, E 1, S 2, W 3
       lat   0
       lon   0]
  (if (empty? stack)
    (prn (+ (Math/abs lat) (Math/abs lon)))
    (let [[instr x] (first stack)]
      (cond
        (= \R instr)
          (recur (rest stack)
                 (mod (+ direc (quot x 90)) 4)
                 lat lon)
        (= \L instr)
          (recur (rest stack)
                 (mod (- direc (quot x 90)) 4)
                 lat lon)
        (or (= \N instr) (= \S instr))
          (recur (rest stack)
                 direc
                 (if (= \N instr) (+ lat x) (- lat x))
                 lon)
        (or (= \E instr) (= \W instr))
          (recur (rest stack)
                 direc
                 lat
                 (if (= \E instr) (+ lon x) (- lon x)))
        (= \F instr)
          (recur (rest stack)
                 direc
                 (if (even? direc)
                   (if (= 0 direc) (+ lat x) (- lat x))
                   lat)
                 (if (odd? direc)
                   (if (= 1 direc) (+ lon x) (- lon x))
                   lon))
        :else (prn " fail: unknown instruction")))))
                 
                
;; -556


