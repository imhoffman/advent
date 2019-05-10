;;  options
(load-option 'format)
;;  constants
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

;; since 'numeric' is built in, no need for this
;(define nums (char-set #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9 #\0))
;(for-each (lambda (s) (display (substring s (string-find-next-char-in-set s nums) (string-find-next-char s #\[)))(newline)) registry)

(for-each
 (lambda (s)
  (display s)
  (display
    (substring s
      (string-find-next-char-in-set s char-set:numeric)
      (string-find-next-char s #\[)))
  (display
    (substring s
      (+ (string-find-next-char s #\[) 1)
      (string-find-next-char s #\])))
  (newline)
  ) registry)


;; scheme seems to lack this
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

(let ((ch "r") (s "baseball been berry berry good to me"))
(format #t "~% The string '~a' has ~a '~a's~%" s (counter (string->char ch) s) (string->char ch))
)

(exit)
