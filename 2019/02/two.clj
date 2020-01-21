(require '[clojure.string :as str])


;;  put all opcode info in a single, large assoc array
;;   key is opcode (currently an int, perhaps it should
;;    be a char like \1, \2 depending on how int-as-str
;;    shakes out when we advance to modality
;;   val is itself a dictionary of properties
(def opcodes
  {1  {:ip-inc 4, :func #(+ (% 1) (% 2))}
   2  {:ip-inc 4, :func #(* (% 1) (% 2))}
   99 {:ip-inc 1, :func #(identity %)}
   })


;;  return a vector of the opcode and its arguments
(defn parse-opcode [ram ip]
  (let [opcode (aget ram ip)]
      (case opcode
        1 (vector opcode
                  (aget ram (aget ram (+ 1 ip)))
                  (aget ram (aget ram (+ 2 ip)))
                  (aget ram (+ 3 ip)))
        2 (vector opcode
                  (aget ram (aget ram (+ 1 ip)))
                  (aget ram (aget ram (+ 2 ip)))
                  (aget ram (+ 3 ip)))
        99 [99])))


;;  apply the opcode function to the arguments
;;  altering the entire, mutable RAM array then
;;  returning the dictionary that includes the ip
(defn operate [ram counters]
  (let [ip          (:ip counters)
        instruction (parse-opcode ram ip)]
    (cond
      (= 99 (instruction 0)) {}     ; empty dict signals halt
      :default (do
        (aset ram (instruction 3) ((:func (opcodes (instruction 0))) instruction))
        (assoc counters :ip (+ ip (:ip-inc (opcodes (instruction 0)))))))))


;;  diagnostic tool
(defn display-ram [ram]
  (println (for [n (range (count ram))] (aget ram n))))


;;  recur until halt
(defn run-program [ram counters]
  (when (not (empty? counters))     ; empty `counters` is the signal to halt
    (recur ram (operate ram counters))))
    ;; `ram` is successfully mutated by `operate` before the function recurs
    ;; even though it seems that `ram` has been passed in its "old" state

;;
;;  main program
;;
;;   file I/O
;;    program will be read into "RAM" as a Java array
;;    so that `aset` can simply modify it, but mutability
;;    is dirty and is messing with my flow!
(def intcode-program
  (let [file-contents (with-open
                        [f (clojure.java.io/reader "puzzle.txt")]
                        (str/trim (slurp f)))]
    (into-array (vec
     (for [c (str/split file-contents #"[,]")]
       (Long/parseLong c))))))        ;; must be parsed as Long for clojure arithmetic

(println "Read" (count intcode-program) "Intcode ints from one line.")

;;  copy program
(def original-program (aclone intcode-program))

;;  part two
(doseq [noun (range 1 100)]
  (doseq [verb (range 1 100)]
    (dotimes
      [i (count intcode-program)]
      (aset intcode-program i (aget original-program i)))
    (aset intcode-program 1 noun)
    (aset intcode-program 2 verb)
    (run-program intcode-program {:ip 0, :base 0})
    (when (= 19690720 (aget intcode-program 0))
      (do
        (println " success with noun--verb combo" (+ (* 100 noun) verb))
        (System/exit 0)))))


