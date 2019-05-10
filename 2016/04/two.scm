(define registry (list))

(define file (open-input-file "input.txt"))
(do ((line (read-line file) (read-line file))) ((eof-object? line))
        (set! registry (append registry (list line))))

(for-each (lambda (s) (display (substring s (string-find-next-char-in-set s (char-set #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9 #\0)) (string-find-next-char s #\[)))(newline)) registry)

