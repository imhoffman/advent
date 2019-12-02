#!/usr/bin/env python3

##
##  subprograms
##

def operator ( program, ip ):
    if program[ip] == 1:
        program[program[ip+3]] = program[program[ip+1]] + program[program[ip+2]]
        return program, ip+4
    elif program[ip] == 2:
        program[program[ip+3]] = program[program[ip+1]] * program[program[ip+2]]
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

#  copy initial program
orig = []
orig[:] = commands[:]

print( "\n read %d commands from input file\n" % ( len(commands) ) )

ip = 0
for noun in range(0,99):
    for verb in range(0,99):
        commands[1] = noun
        commands[2] = verb
        while ip != -1:
            commands, ip = operator( commands, ip)
        if commands[0] == 19690720:
            print( "\n output: %d, noun: %d, verb: %d\n\n" %
                    ( commands[0], noun, verb ) )
            break;
        else:
            commands[:] = orig[:]   # reload program
            ip = 0


