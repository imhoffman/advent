
;;  example program from puzzle description
(def program [1 9 10 3
              2 3 11 0
              99
              30 40 50])


;;  dictionary of {opcode, ip increment}
(def opcode-widths {1 4, 2 4, 99 0})


;;  dictionary of {opcode, function}
(def operations
  {1 #(+ (% 1) (% 2)),
   2 #(* (% 1) (% 2))})


;;  return a vector of the opcode and its arguments
(defn parse-opcode [ram ip]
  (into []
        (for
          [n (range ip (+ ip (opcode-widths (ram ip))))]
          (ram n))))


;;  apply the opcode function to the arguments
(defn operate [ram ip]
  (let [instruction (parse-opcode ram ip)]
    ((operations (instruction 0)) instruction)))


;;  test print
(println (operate program 4))


