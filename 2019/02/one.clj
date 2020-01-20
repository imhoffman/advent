(require '[clojure.string :as str])



;;  put all opcode info in a single, large assoc array
;;   key is opcode (currently an int, perhaps it should
;;    be a char like \1, \2 depending on how int-as-str
;;    shakes out when we advance to modality
;;   val is itself a dictionary of properties
(def opcodes
  {1  {:ip-inc 4, :func #(+ (% 1) (% 2))}
   2  {:ip-inc 4, :func #(* (% 1) (% 2))}
   99 {:ip-inc 0, :func #(identity %)}
   })

;;  perhaps some sort of mutable bookkeeping ?
;;   ... hopefully not, simply `(assoc state :ip ...)` should do
(def state  ; (to-array ???
  {:ip 0
   :base 0})


;;  return a vector of the opcode and its arguments
(defn parse-opcode [ram ip]
  (into []
        (for
          [n (range ip (+ ip (:ip-inc (opcodes (aget ram ip)))))]
          (aget ram n))))


;;  apply the opcode function to the arguments
;;  altering the entire, mutable RAM array
(defn operate [ram ip]
  (let [instruction (parse-opcode ram ip)]
    (aset ram (instruction 3) ((:func (opcodes (instruction 0))) instruction))))



;;  diagnostic tool
(defn display-ram [ram]
  (println (for [n (range (count ram))] (aget ram n))))

;;
;;  main program
;;
;;   file I/O
;;    program will be read into "RAM" as a Java array
;;    so that `aset` can simply modify it
(def intcode-program
  (let [file-contents (with-open
                        [f (clojure.java.io/reader "puzzle.txt")]
                        (str/trim (slurp f)))]
    (into-array (vec
     (for [c (str/split file-contents #"[,]")]
       (Long/parseLong c))))))        ;; must be parsed as Long for clojure arithmetic

(println "Read" (count intcode-program) "Intcode ints from one line.")


;;  test prints
(display-ram intcode-program)
(operate intcode-program 0)
(display-ram intcode-program)


