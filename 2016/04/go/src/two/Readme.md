GO experiments
==============

## environment and path

https://golang.org/doc/install
https://golang.org/doc/code.html
don't forget to export the GOPATH to the go above the src
$ cd ../..
$ export GOPATH=`pwd`
that way, the following will work
$ go build
$ ./two     <--- enormous static executable
and maybe even
$ go install
rather than simply
$ go run two.go
and, I'm breaking the rules by symlinking the puzzle input file

## Concurrent channels

```
 Performance counter stats for '/usr/local/go/bin/go run vs.go' (10 runs):

        167.802179      task-clock (msec)         #    1.436 CPUs utilized            ( +-  1.76% )
             1,456      context-switches          #    0.009 M/sec                    ( +-  0.76% )
               154      cpu-migrations            #    0.917 K/sec                    ( +-  3.80% )
            21,490      page-faults               #    0.128 M/sec                    ( +-  0.92% )
       655,224,471      cycles                    #    3.905 GHz                      ( +-  1.63% )  (34.37%)
     1,132,353,082      instructions              #    1.73  insn per cycle           ( +-  3.45% )  (54.50%)
       214,006,006      branches                  # 1275.347 M/sec                    ( +-  3.61% )  (64.32%)
         2,296,197      branch-misses             #    1.07% of all branches          ( +-  1.89% )  (72.54%)
       253,611,150      L1-dcache-loads           # 1511.370 M/sec                    ( +-  2.40% )  (79.78%)
         8,575,072      L1-dcache-load-misses     #    3.38% of all L1-dcache hits    ( +-  1.86% )  (74.86%)
         2,105,156      LLC-loads                 #   12.545 M/sec                    ( +-  8.43% )  (41.58%)
           943,503      LLC-load-misses           #   44.82% of all LL-cache hits     ( +- 12.81% )  (32.55%)

       0.116862908 seconds time elapsed                                          ( +-  0.78% )
```

```
 Performance counter stats for '/usr/local/go/bin/go run chans.go' (10 runs):

        167.693575      task-clock (msec)         #    1.432 CPUs utilized            ( +-  1.72% )
             1,496      context-switches          #    0.009 M/sec                    ( +-  0.69% )
               155      cpu-migrations            #    0.923 K/sec                    ( +-  2.94% )
            21,289      page-faults               #    0.127 M/sec                    ( +-  0.74% )
       667,237,620      cycles                    #    3.979 GHz                      ( +-  1.82% )  (36.23%)
     1,185,041,808      instructions              #    1.78  insn per cycle           ( +-  4.16% )  (57.70%)
       214,941,006      branches                  # 1281.749 M/sec                    ( +-  4.18% )  (65.79%)
         2,283,287      branch-misses             #    1.06% of all branches          ( +-  1.94% )  (70.54%)
       258,412,812      L1-dcache-loads           # 1540.982 M/sec                    ( +-  2.48% )  (76.53%)
         8,447,552      L1-dcache-load-misses     #    3.27% of all L1-dcache hits    ( +-  1.92% )  (73.51%)
         2,026,598      LLC-loads                 #   12.085 M/sec                    ( +-  4.99% )  (41.90%)
           767,073      LLC-load-misses           #   37.85% of all LL-cache hits     ( +- 10.96% )  (35.50%)

       0.117095262 seconds time elapsed                                          ( +-  0.85% )
```
