(require '[clojure.string :as str])


;;  put all opcodes info in a single, large assoc array
;;   key is opcode (currently an int, perhaps it should
;;    be a char like \1, \2 depending on how int-as-str
;;    shakes out when we advance to modality)
;;   val is itself a dictionary of properties
(def opcodes
  {1  {:ip-inc 4, :func #(+ (% 1) (% 2))}
   2  {:ip-inc 4, :func #(* (% 1) (% 2))}
   99 {:ip-inc 1, :func #(identity %)}
   })


;;  parse the Intcode instruction
;;  return a vector of the opcode and its arguments
(defn parse-opcode [ram ip]
  (let [opcode (ram ip)]
      (case opcode
        1 (vector opcode
                  (ram (ram (+ 1 ip)))
                  (ram (ram (+ 2 ip)))
                  (ram (+ 3 ip)))
        2 (vector opcode
                  (ram (ram (+ 1 ip)))
                  (ram (ram (+ 2 ip)))
                  (ram (+ 3 ip)))
        99 [99])))


;;  execute a single operation
;;  look up the `func` in the `opcode` dict and act that func on
;;   the vector of instructions, then `assoc` the func return into
;;   the RAM vector at the appropriate address as per `(instruction 3)`
;;  return the dictionary that includes the new ip along with the
;;   new RAM vector
(defn operate [ram dict-of-counters]
  (let [ip          (:ip dict-of-counters)
        instruction (parse-opcode ram ip)]
    (cond
      (= 99 (instruction 0)) (vector ram {})     ;;  *** empty dict signals halt ***
      :default (vector
                 (assoc ram (instruction 3) ((:func (opcodes (instruction 0))) instruction))
                 (assoc dict-of-counters :ip (+ ip (:ip-inc (opcodes (instruction 0)))))))))



;;  run the program by recurring over RAM and IP until halt
(defn run-program [ram counters-dict]
  (if (empty? counters-dict)             ; empty `counters-dict` is the signal to halt
    (do
      (println " The program has halted, run-program is returning the RAM state.")
      ram)
    (let [[a,b] (operate ram counters-dict)] ;;  destructure return
      (recur a b))))                         ;;  how to recur w/o `let` destructuring?


;;
;;  main program
;;
;;   file I/O
;;    program will be read into "RAM" as a vector for subsequent `assoc`s
(def intcode-program
  (let [file-contents (with-open
                        [f (clojure.java.io/reader "puzzle.txt")]
                        (str/trim (slurp f)))]
    (vec
      (for [c (str/split file-contents #"[,]")]
        (Long/parseLong c)))))

(println "Read" (count intcode-program) "Intcode ints from one line.")


;;  initialize the run with the dictionary of counters that is to
;;   be recursively returned
;;    start with an `ip` of 0 and with the program loaded into RAM
;;    input the `12` and the `2` as per the puzzle
;;  `run-program` will return the current RAM vector upon halt
(println " address 0 in RAM currently holds"
         ((run-program
            (#(-> %                  ;; the RAM argument to `run-program`
                  (assoc 1 12)
                  (assoc 2 2))
                  intcode-program)
            {:ip 0, :base 0})        ;; the counter dict argument
          0))                        ;; retreive the zeroth element of RAM upon return


