#!/usr/bin/env python3

##
##  subprograms
##

def operator ( program, ip ):
    if program[ip] == 1:
        program[program[ip+3]] = program[ip+1] + program[ip+2]
        return program, ip+4
    elif program[ip] == 2:
        program[program[ip+3]] = program[ip+1] * program[ip+2]
        return program, ip+4
    elif program[ip] == 99:
        return program, -1   # catch -1 in main and halt


##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

commands = line.rstrip().split(sep=",")
commands = [ int( s ) for s in commands ]

print( "\n read %d commands from input file\n" % ( len(commands) ) )

ip = 0
while True:
    commands, ip = operator( commands, ip)
    if ip == -1:
        break;

print( "\n the value at position 0 after halting is %d\n\n" % commands[0] )


