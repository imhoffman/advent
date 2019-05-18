GO experiments
==============

## environment and path

Unless using simply `go run two.go` at the bash prompt, the directory structure above this Readme is mandated, as described [here](https://golang.org/doc/install) and [here](https://golang.org/doc/code.html).
To do anything besides `run`, the `GOPATH` variable needs to be set.
From here, it is
```
$ cd ../..
$ export GOPATH=`pwd`
```
Once set, the following will work
```
$ go build
$ ./two
```
where `two` is an enormous static executable.
Also, I'm breaking the rules by symlinking the puzzle input file.

## Concurrent channels

I am trying to learn concurrency.
The experimental code is in `chans.go`.
My goal is to get the many different operations (e.g., `sumcheck`) on the input list to run asynchronously by making use of something like `d := make( chan int, len(r) )`.
For the synchronous case, it performs as follows:

```
 Performance counter stats for './two' (128 runs):

          1.866827      task-clock (msec)         #    1.069 CPUs utilized            ( +-  0.45% )
                47      context-switches          #    0.025 M/sec                    ( +-  1.20% )
                 1      cpu-migrations            #    0.703 K/sec                    ( +-  8.85% )
               207      page-faults               #    0.111 M/sec                    ( +-  0.26% )
         4,505,656      cycles                    #    2.414 GHz                      ( +-  2.09% )  (82.49%)
         6,989,662      instructions              #    1.55  insn per cycle           ( +-  0.11% )
         1,425,913      branches                  #  763.816 M/sec                    ( +-  0.10% )
            18,717      branch-misses             #    1.31% of all branches          ( +-  0.35% )
         1,981,941      L1-dcache-loads           # 1061.663 M/sec                    ( +-  0.13% )
            52,586      L1-dcache-load-misses     #    2.65% of all L1-dcache hits    ( +-  0.42% )
     <not counted>      LLC-loads                                                     (0.00%)
     <not counted>      LLC-load-misses                                               (0.00%)

       0.001746484 seconds time elapsed                                          ( +-  0.35% )
```

