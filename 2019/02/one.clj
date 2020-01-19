
(def program [1 9 10 3
              2 3 11 0
              99
              30 40 50])

(defn parse-opcode [ram ip]
  (let [opcode-widths {1 4, 2 4, 99 1}]
    (into []
          (for [n (range ip (+ ip (opcode-widths (get ram ip))))]
            (get ram n)))))

;(defn operations {1 #(+ %1 %2), 2 #(* %1 %2)})

(println (parse-opcode program 4))

