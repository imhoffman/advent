GO experiments
==============

## environment and path

Unless using simply `go run two.go` at the bash prompt, the directory structure above this Readme is mandated, as described [here](https://golang.org/doc/install) and [here](https://golang.org/doc/code.html).
The `GOPATH` variable needs to be set.
From this directory, it is
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
This is the executable that I use in the benchmarking below.
Usually, I simply use `run`.

BTW, I'm breaking the rules by symlinking the puzzle input file.

## concurrent channels

The code in `chans.go` uses concurrency.

My goal is to get the many different operations (e.g., `sumcheck`) on the input list to run asynchronously on the channel `d := make( chan int, len(r) )`.
However, my simple solution of waiting for `len(d) == cap(d)` does not work.
(I eventually figured it out! Keep reading!)
For the synchronous case (that is, blocking each routine in a queue for single channel, which is basically a really tricked-out for loop), it performs as follows:

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

I figured out asynchronous channelling! WaitGroups!
FWIW, here is the asynchronous performance of `chans.go` on the same linux Intel(R) Core(TM) i7-7700 CPU @ 3.60GHz.

```
 Performance counter stats for './two' (128 runs):

          2.469306      task-clock (msec)         #    1.950 CPUs utilized            ( +-  1.24% )
                38      context-switches          #    0.015 M/sec                    ( +-  1.87% )
                 5      cpu-migrations            #    0.002 M/sec                    ( +-  4.95% )
               466      page-faults               #    0.189 M/sec                    ( +-  2.02% )
         9,097,435      cycles                    #    3.684 GHz                      ( +-  1.69% )  (88.87%)
         8,591,258      instructions              #    0.94  insn per cycle           ( +-  0.59% )
         1,750,957      branches                  #  709.089 M/sec                    ( +-  0.59% )
            19,457      branch-misses             #    1.11% of all branches          ( +-  0.82% )
         2,411,292      L1-dcache-loads           #  976.506 M/sec                    ( +-  0.66% )
           140,939      L1-dcache-load-misses     #    5.84% of all L1-dcache hits    ( +-  0.73% )
     <not counted>      LLC-loads                                                     (0.00%)
     <not counted>      LLC-load-misses                                               (0.00%)

       0.001266223 seconds time elapsed                                          ( +-  1.26% )
```

