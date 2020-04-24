
## performance investigation

Without any [transients](https://clojure.org/reference/transients) in the code, it runs as

```
13.2684 +- 0.0311 seconds time elapsed  ( +-  0.23% )
```


For the code with transients as in the repo, it runs as

```
6.9230 +- 0.0415 seconds time elapsed  ( +-  0.60% )
```


With transients but without [arbitrary-precision integers](https://clojure.org/guides/learn/syntax#_numeric_types), it is

```
6.2999 +- 0.0432 seconds time elapsed  ( +-  0.68% )
```

I went with a big int out of the gate for part two because Eric always has me on my toes!
