Go experiments
==============

## puzzle solution

I have a working solution in [`two.go`](two.go).

My first asynchronous solution is [`race.go`](race.go) which exhibited an interesting failure owing to a race condition that I eventually found (as described at the bottom of the Readme).
The code always retruns the correct answer for the sum of the sector id's when it is run on my chromebook (chromebrew go version go1.12 linux/amd64) but almost always returns a value that is slightly too low for the sum when run on my much faster linux desktop (go version go1.12.5 linux/amd64).
Even if I compile the binary on my chromebook and run that binary on my desktop, the problem persists.

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
This is the executable that I describe below in the performance analysis.
Usually, I simply use `run`.

BTW, I'm breaking the rules by symlinking the puzzle input file.

## concurrent channels

The code in `chans.go` uses concurrency.

My goal is to get the many different operations (e.g., `sumcheck`) on the input list to run asynchronously on the channel `d := make( chan int, len(r) )`.
However, my simple solution of waiting for `len(d) == cap(d)` does not work.
(I eventually figured it out! Keep reading!)
For the synchronous case (that is, blocking each routine in a queue for a single channel `d := make( chan int )`, which is basically a really tricked-out for loop), it performs as follows:

```
 Performance counter stats for './two' (128 runs):

          1.408241      task-clock (msec)         #    1.045 CPUs utilized            ( +-  0.36% )
                38      context-switches          #    0.027 M/sec                    ( +-  1.22% )
                 2      cpu-migrations            #    0.001 M/sec                    ( +-  7.13% )
               206      page-faults               #    0.147 M/sec                    ( +-  0.25% )
         4,573,914      cycles                    #    3.248 GHz                      ( +-  1.21% )  (87.64%)
         6,908,291      instructions              #    1.51  insn per cycle           ( +-  0.10% )
         1,410,601      branches                  # 1001.676 M/sec                    ( +-  0.09% )
            18,243      branch-misses             #    1.29% of all branches          ( +-  0.33% )
         1,953,974      L1-dcache-loads           # 1387.528 M/sec                    ( +-  0.12% )
            51,125      L1-dcache-load-misses     #    2.62% of all L1-dcache hits    ( +-  0.41% )
     <not counted>      LLC-loads                                                     (0.00%)
     <not counted>      LLC-load-misses                                               (0.00%)

       0.001347552 seconds time elapsed                                          ( +-  0.27% )
```

I figured out asynchronous channelling! WaitGroup!
FWIW, here is the asynchronous performance of `chans.go` for the same number of context switches on the same linux box (Intel(R) Core(TM) i7-7700 CPU @ 3.60GHz).

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

### concurrency without a channel

~~It turns out that I don't need to use a channel for communication at all.
The `go` routine handler is smart enough to manage multiple writes to the same variable, so I simply passed the address of the variable that would hold the sum and all of the routines write to it from within their functions (and then `main()` must still wait on the WaitGroup).~~
(False: All of the goroutines writing to the same pointer is indeed a race.
A similar problem is described [at the golang site](https://blog.golang.org/race-detector).
By using the `-race` flag, the problem is caught.
My final solution uses the WaitGroup and a loop through the buffered channel; perhaps there is a better way, like pushing and popping the jobs out of a ring pool or something...)
That is,
```
func checker ( s string, sum *int, group *sync.WaitGroup )
```
rather than
```
func checker ( s string, c chan int, group *sync.WaitGroup )
```
In this way, I skip both the need to return a zero on the channel in the `sumcheck` if and the need to sum over the channel from main.
The codes for synchronous channel, asynchronous channel, and no channel are `synchronous.go`, `chans.go`, and `asynchronous.go`, respectively.

The perf stats for `asynchronous.go` are

```
 Performance counter stats for './two' (128 runs):

          2.177258      task-clock (msec)         #    1.847 CPUs utilized            ( +-  0.91% )
                38      context-switches          #    0.018 M/sec                    ( +-  1.34% )
                 5      cpu-migrations            #    0.002 M/sec                    ( +-  5.41% )
               443      page-faults               #    0.204 M/sec                    ( +-  1.37% )
         8,038,551      cycles                    #    3.692 GHz                      ( +-  2.01% )  (86.42%)
         8,321,258      instructions              #    1.04  insn per cycle           ( +-  0.47% )
         1,687,349      branches                  #  774.988 M/sec                    ( +-  0.51% )
            18,888      branch-misses             #    1.12% of all branches          ( +-  0.62% )
         2,337,590      L1-dcache-loads           # 1073.640 M/sec                    ( +-  0.59% )
           133,750      L1-dcache-load-misses     #    5.72% of all L1-dcache hits    ( +-  0.55% )
     <not counted>      LLC-loads                                                     (0.00%)
     <not counted>      LLC-load-misses                                               (0.00%)

       0.001178824 seconds time elapsed                                          ( +-  0.80% )
```
