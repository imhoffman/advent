#!/usr/bin/env python3

##
##  subprograms
##

def operator ( program ):
    for i in range( 0, len(program), 4 ):
        print( program[i] )

##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

commands = line.split(sep=",")

print( "\n read %d commands from input file\n" % ( len(commands) ) )

operator( commands)


