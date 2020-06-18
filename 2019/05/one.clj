(require '[clojure.string :as str])


;;  put all opcodes info in a single, large assoc array
;;   key is opcode (currently an int, perhaps it should
;;    be a char like \1, \2 depending on how int-as-str
;;    shakes out when we advance to modality)
;;   val is itself a dictionary of properties
(def opcodes
  {1  {:ip-inc 4, :func #(+ (% 1) (% 2))}
   2  {:ip-inc 4, :func #(* (% 1) (% 2))}
   3  {:ip-inc 2, :func (fn [& xs] (Long/parseLong (read-line)))}
   4  {:ip-inc 2, :func #((println " the progam outputs" (% 1)))}
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
;;   the vector `instruction`, then `assoc` the func return into
;;   the RAM vector at the appropriate address as per `(last instruction)`
;;  return the dictionary that includes the new ip along with the
;;   new RAM vector
(defn operate [ram dict-of-counters]
  (let [ip           (dict-of-counters :ip)
        instruction  (parse-opcode ram ip)
        opcode       (instruction 0)
        opcode-dict  (opcodes opcode)
        operation    (opcode-dict :func)
        ip-increment (opcode-dict :ip-inc)]
    (vector
      (case opcode            ;;  RAM return
        (99, 4) ram           ;;  unaltered RAM for 99, 4
                (assoc        ;;  else
                  ram (last instruction) (operation instruction)))
      (case opcode            ;;  counter return
        99      {}            ;;  *** empty IP dict signals halt ***
                (assoc        ;;  else
                  dict-of-counters
                  :ip (+ ip ip-increment))))))



;;  run the program by recurring over RAM and IP until halt
(defn run-program [ram counters-dict]
  (if (empty? counters-dict)                 ;;  *** empty `counters-dict` signals halt ***
    ram                                      ;;  return RAM vector at halt
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


;;  brute loop through the possibilities
(doseq [noun (range 1 100)]
  (doseq [verb (range 1 100)]
    (when (= 19690720
             ((run-program               ;; is the RAM vector upon return
                (#(-> %                  ;; the RAM argument to `run-program`
                      (assoc 1 noun)
                      (assoc 2 verb))
                      intcode-program)
                {:ip 0, :base 0})        ;; the counter dict argument
              0))                        ;; retreive the zeroth element of RAM upon return
      (do
        (println " success with noun--verb combo" (+ (* 100 noun) verb))
        (System/exit 0)))))              ;; poor man's bail out


