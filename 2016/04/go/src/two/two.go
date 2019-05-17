package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
)

const max_lines int = 16384

//  file read
//   https://golang.org/pkg/bufio/#Scanner
//   https://stackoverflow.com/questions/8757389/reading-file-line-by-line-in-go
//  OMG!  from
//   https://golang.org/doc/effective_go.html#data
//   "Note that, unlike in C, it's perfectly OK to return the address of a local variable; the storage associated with the variable survives after the function returns."
func reader () []string {
    var i int = 0
    temp := make( []string, max_lines )

    f, err := os.Open("input.txt")
    if err != nil { log.Fatal(err) }
    defer f.Close()
    s := bufio.NewScanner(f)
    for s.Scan() {
      temp[i] = s.Text()
      i++
    }
    if err := s.Err(); err != nil { log.Fatal(err) }

    //  https://blog.golang.org/go-slices-usage-and-internals
    registry := make( []string, i )
    copy( registry, temp )
    return registry
}


//  main program
func main () {

    r := reader()
    for _, s := range r { fmt.Println( s ) }

    fmt.Printf("\n read %d lines\n\n", len(r) )

}
