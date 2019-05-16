package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
)

// file read
//  https://golang.org/pkg/bufio/#Scanner
//  https://stackoverflow.com/questions/8757389/reading-file-line-by-line-in-go
func reader ( Nrooms *int ) {
    var i int = 0

    f, err := os.Open("input.txt")
    if err != nil { log.Fatal(err) }
    defer f.Close()
    s := bufio.NewScanner(f)
    for s.Scan() {
      fmt.Println(s.Text())
      i++
    }
    if err := s.Err(); err != nil { log.Fatal(err) }

    *Nrooms = i
    return
}

func main () {
    var Nrooms int

    reader ( &Nrooms )

    fmt.Printf("\n read %d lines\n\n", Nrooms)

}
