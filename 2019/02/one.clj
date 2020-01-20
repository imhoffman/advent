(require '[clojure.string :as str])


;;  program counter lookup
;;  dictionary of {opcode, ip increment}
(def opcode-widths {1 4, 2 4, 99 0})


;;  dictionary of {opcode, function}
;;   perhaps the keys should be chars like \1, \2
;;   depending on how to int-as-str shakes out
(def operations
  {1 #(+ (% 1) (% 2)),
   2 #(* (% 1) (% 2))})


;;  perhaps a single, large assoc array ?
;(def opcodes
;  {1 {:ip-inc 4, :func #(+ (% 1) (% 2))}


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


;;
;;  main program
;;
;;   file I/O
(def intcode-program
  (let [file-contents (with-open
                        [f (clojure.java.io/reader "puzzle.txt")]
                        (str/trim (slurp f)))]
    (vec
     (for [c (str/split file-contents #"[,]")]
       (Integer/parseInt c)))))

(println "Read" (count intcode-program) "Intcode ints from one line.")

;;  test print
(println intcode-program)
(println (operate intcode-program 0))


