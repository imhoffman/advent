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

The `chans.go` file is me messing with getting the operations on the input file to run asynchronously.
Very much a work in progress...

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
