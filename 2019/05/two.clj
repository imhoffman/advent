(require '[clojure.string :as str])


;;  put all opcodes info in a single, large assoc array
;;   key is opcode as a char, treating "99" as simply \9
;;   val is itself a dictionary of properties
(def opcodes
  {\1 {:ip-inc 4, :func #(+ (% 1) (% 2))}
   \2 {:ip-inc 4, :func #(* (% 1) (% 2))}
   \3 {:ip-inc 2, :func (fn [&x]
                          (print " input: ") (flush)
                          (Long/parseLong (read-line)))}
   \4 {:ip-inc 2, :func #(let [out (% 1)]
                           (println " the progam outputs:" out)
                           out)}       ;; also return what is printed
   \5 {:ip-inc 3, :func #(not= 0 (% 1))}
   \6 {:ip-inc 3, :func #(= 0 (% 1))}
   \7 {:ip-inc 4, :func #(if (< (% 1) (% 2)) 1 0)}
   \8 {:ip-inc 4, :func #(if (= (% 1) (% 2)) 1 0)}
   \9 {:ip-inc 1, :func #(identity %)}
   })


;;  parse the Intcode instruction
;;  return a vector of the opcode and its arguments
(defn parse-opcode [ram ip]
  (let [opcode    (ram ip)
        charcode  (vec (map char (str opcode)))        ;; Intcode as vec of chars
        positions (vec (map char (reverse "ABCDE")))   ;; modality position names for keys
        offsets   {\C 1, \B 2, \A 3}                   ;; relative location of value for mode args
        parmsdict (loop [charstack (reverse charcode)  ;; dictionary ABCDE values
                         outdict   {}
                         counter   0]
                    (if (empty? charstack)     ;; when done, amend implicit leading zeroes
                      (loop [out      outdict
                             keystack (nthrest positions counter)]
                        (if (empty? keystack)
                          out
                          (recur (assoc out (first keystack) \0) (rest keystack))))
                      (recur                   ;; work through chars in the Intcode op
                        (vec (rest charstack))
                        (assoc outdict (positions counter) ((vec (reverse charcode)) counter))
                        (inc counter))))]
    (letfn [(pos [charkey] (ram (ram (+ (offsets charkey) ip))))
            (imm [charkey] (ram      (+ (offsets charkey) ip)))]
      (let [opchar (parmsdict \E)]
        (case opchar
          (\1,\2,\7,\8)
              (vector opchar
                      (if (= \0 (parmsdict \C)) (pos \C) (imm \C))
                      (if (= \0 (parmsdict \B)) (pos \B) (imm \B))
                      (ram (+ 3 ip)))         ;; "will never be in immediate mode"
          \3  (vector opchar
                      (ram (+ 1 ip)))         ;; "will never be in immediate mode"
          \4  (vector opchar
                      (if (= \0 (parmsdict \C)) (pos \C) (imm \C)))
          (\5,\6)
              (vector opchar
                      (if (= \0 (parmsdict \C)) (pos \C) (imm \C))
                      (if (= \0 (parmsdict \B)) (pos \B) (imm \B)))
          \9  [opchar])))))


;;  execution routine for a single operation
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
        (\9,\5,\6)
           ram                ;;   unaltered RAM for 99, 5, and 6
        \4 (do                ;;   side-effect for 4 ...
             (operation instruction)
             ram)             ;;   ...then return unaltered RAM
           (assoc             ;;   else return altered RAM
                  ram (last instruction) (operation instruction)))
      (case opcode            ;;  counter return
        \9 {}                 ;;   *** empty IP dict signals halt ***
        (\5,\6)
           (if (operation instruction)
             (assoc dict-of-counters
                    :ip (instruction 2))
             (assoc dict-of-counters
                    :ip (+ ip ip-increment)))
           (assoc             ;;   else update counter(s)
                  dict-of-counters
                  :ip (+ ip ip-increment))))))



;;  run the program by recurring over RAM and IP until halt
(defn run-program [ram counters-dict]
  (if (empty? counters-dict)                 ;;  *** empty `counters-dict` signals halt ***
    ram                                      ;;  return RAM vector at halt
    (let [[a,b] (operate ram counters-dict)] ;;  else recur w/ destructured return
      (recur a b))))                         ;;   how to recur w/o `let` destructuring?


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
;;   end file I/O



;;  load the program and run it beginning with IP of 0
(run-program
  intcode-program
  {:ip 0, :base 0})

