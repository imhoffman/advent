;;
;; unused range function
(defun range (min max &optional (step 1))
  (when (<= min max)
    (cons min (range (+ min step) max step)))
)
;;(print (range 0 8))

;;
;;  character counter...though I have since learned about 'count'
(defun counter (ch str)
  (let ((i 0))
  (loop for j from 0 to (- (length str) 1) do
    (if (string= ch (char str j)) (setf i (+ i 1))))
  i
  )
)
(setf s (string "the change has come she's under my thumb"))
(setf ch "e")
(format t "~& '~a' has ~d '~a's in it~%" s (counter ch s) ch)
(format t "~& '~a' has ~d '~a's in it~%" s (count (coerce "e" 'character) s) ch)

;;
;;  numeral locator...though I have since learned about 'count-if'
(defun isdigit (ch)
  (let ((nums (string "0123456789")))
    (loop for j from 0 to (- (length nums) 1) do
      (if (string= ch (char nums j)) (return-from isdigit t))
    )
    nil
  )
)
(setf s "7")
(format t "~& ~a is~aa digit~%" s (if (isdigit s) (string " ") (string " not ")))
(format t "~& ~a is~aa digit~%" s (if (= 1 (count-if #'digit-char-p s)) (string " ") (string " not ")))

;;
;;  extract sector id from file listing
(defun get_id (str)
  (let ((val (list)))
    (loop for j from 0 to (- (length str) 1) do
      (if (isdigit (char str j)) (push (char str j) val))
    )
    (nreverse val)
    (parse-integer (concatenate 'string val))
  )
)

;;
;;  main program
;;
;; Does my read loop involve full traversal ?
;;  https://stackoverflow.com/questions/13359025/adding-to-the-end-of-list-in-lisp
;; should the push instead be a lambda that acts like the range function above?
(setf registry (list))
(let ((fp (open "input.txt" :if-does-not-exist nil)))
  (when fp
    (loop for line = (read-line fp nil)
      while line do (push line registry))
    (close fp)
  )
)
(setf registry (reverse registry))

(loop for s in registry do
  (format t "~& ~03d ~a~%" (get_id s) s))

