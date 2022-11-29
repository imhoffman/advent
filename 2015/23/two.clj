
(require '[clojure.string :as str])

(def program (->> "puzzle.txt"
                  slurp
                  (#(str/split % #"\n") ,,)
                  (map #(re-matches #"([a-z]{3}) ([ab]*)(?:\, )*([\+\-]{0,1}\d*)" %) ,,)
                  (map #(into {} (map vector [:op :reg :offset] (rest %))) ,,)
                  (mapv #(reduce (fn [a b]
                                  (assoc a (first b)
                                         (cond
                                           (empty? (second b))
                                           nil

                                           (re-matches #".*?\d+" (second b))
                                           (Integer/parseInt (second b))

                                           :else
                                           (second b))))
                                {} %) ,,)))


(println program)

(defn operate [d]
  (let [ip    (d :ip)
        instr (program ip)
        op    (instr :op)
        reg   (instr :reg)
        off   (instr :offset)]
    ;(println " working on" instr)
    (cond
      (= "hlf" op)
      (assoc d reg (/ (d reg) 2)
               :ip  (inc ip))

      (= "inc" op)
      (assoc d reg (inc (d reg))
               :ip  (inc ip))

      (= "tpl" op)
      (assoc d reg (* 3 (d reg))
               :ip  (inc ip))

      (= "jmp" op)
      (assoc d :ip (+ ip off))

      (= "jie" op)
      (assoc d :ip (if (even? (d reg)) (+ ip off) (inc ip)))

      (= "jio" op)
      (assoc d :ip (if (= 1 (d reg)) (+ ip off) (inc ip)))

      :else
      (println " unknown op"))))



(loop [s {:ip 0, "a" 1, "b" 0}]
  (if (>= (s :ip) (count program))
    (println " done:" s)
    (recur (operate s))))

                  

