;;  options
;;   as per https://www.gnu.org/software/mit-scheme/documentation/mit-scheme-ref/Format.html
(load-option 'format)

;;  constants
;;   as a list, will string->list as needed for char-set
(define alpha "abcdefghijklmnopqrstuvwxyz")

;; empty list to eventually hold contents of input file
(define registry (list))
;; read in the file
;;  perhaps cons a (string line) rather than redefining registry,
;;  that way the close could be inside a lambda, a la
;;  https://scheme.com/tspl4/io.html#./io:h9
(define file (open-input-file "input.txt"))
(do ((line (read-line file) (read-line file))) ((eof-object? line))
        (set! registry (append registry (list line))))
(close-input-port file)


;;  diagnose read and parsing
;(for-each
; (lambda (s)
;  (format #t " ~a has id: ~a and checksum: ~a~%"
;   s
;   (substring s
;      (string-find-next-char-in-set s char-set:numeric)
;      (string-find-next-char s #\[))
;   (substring s
;      (+ 1 (string-find-next-char s #\[))
;      (string-find-next-char s #\]))
;  )) registry)


;; scheme seems to lack this function
(define string->char
 (lambda (s)
  (car (string->list s))))


;;  recursive occurrence counter
(define counter
 (lambda (ch s)
  (let ((k (string-find-next-char s ch)))
   (if k
    (begin (if (= k (string-length s))
            1
            (+ 1 (counter ch (substring s (+ 1 k) (string-length s))))))
   0))))


(define sumcheck
 (lambda (s)
  (let ( (id (substring s
              (string-find-next-char-in-set s char-set:numeric)
              (string-find-next-char s #\[)))
         (checksum (substring s
                    (+ 1 (string-find-next-char s #\[))
                    (string-find-next-char s #\])))
         (test (car (string->list "ebchf"))) )
  (format #t " The string '~a' has ~a '~a's~%" s (counter test s) test))))

(for-each sumcheck registry)

(exit)
