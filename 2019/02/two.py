#!/usr/bin/env python3

##
##  subprograms
##

def operator ( program, ip ):
    if program[ip] == 1:
        program[ program[ip+3] ] = \
                program[ program[ip+1] ] + program[ program[ip+2] ]
        return program, ip+4
    elif program[ip] == 2:
        program[ program[ip+3] ] = \
                program[ program[ip+1] ] * program[ program[ip+2] ]
        return program, ip+4
    elif program[ip] == 99:
        return program, -1          # catch -1 in main and halt


##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

commands = [ int( s ) for s in line.rstrip().split(sep=",") ]

print( "\n read %d commands from input file\n" % ( len(commands) ) )

#  copy initial program
orig = []
orig[:] = commands[:]               # without [:], this is a pointer

ip = 0                              # aka program counter
done = False
for noun in range(0,100):           # 0--99 inclusize, as per rules
    for verb in range(0,100):
        commands[1] = noun
        commands[2] = verb
        while ip != -1:
            commands, ip = operator( commands, ip)
        if commands[0] == 19690720:
            print( "\n output: %d, noun: %d, verb: %d, answer: %d\n\n" %
                    ( commands[0], noun, verb, 100*noun+verb ) )
            done = True
            break                   # break inner loop
        else:
            commands[:] = orig[:]   # reload program
            ip = 0                  # reset program counter (!)
    if done:
        break                       # break outer loop


