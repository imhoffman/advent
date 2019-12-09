#!/usr/bin/env python3

import numpy as np

##
##  subprograms
##

#  unpack individual integers from compound Intcode
def parse_opcode ( ABCDE ):
    A, B, C = 0, 0, 0
    as_char_array = str( ABCDE )
    opcode = int( as_char_array[-1:] )
    if opcode == 9 and len(as_char_array) > 1 and int( as_char_array[-2:-1] ) == 9:
        opcode = 99
    if as_char_array[-3:-2]:
        C = int( as_char_array[-3:-2] )
    if as_char_array[-4:-3]:
        B = int( as_char_array[-4:-3] )
    if as_char_array[-5:-4]:
        A = int( as_char_array[-5:-4] )
    return opcode, (C, B, A)

#  for fetching the appropriate value as per the instruction modalities
#   "Parameters that an instruction writes to will never be in immediate mode."
def modal_parameters ( opcode, ram, ip, modes, base ):
    if opcode == 99:
        return 0, 0, 0
    # first param; arg1 is the writing location of opcode 3
    if opcode == 3:
        if modes[0] == 2:
            arg1 = ram[ip+1] + base
        else:
            arg1 = ram[ip+1]
    else:
        if modes[0] == 1:
            arg1 = ram[ip+1]
        elif modes[0] == 2:
            arg1 = ram[ ram[ip+1] + base ]
        else:
            arg1 = ram[ ram[ip+1] ]
    # second param
    if modes[1] == 1:
        arg2 = ram[ip+2]
    elif modes[1] == 2:
        arg2 = ram[ ram[ip+2] + base ]
    else:
        arg2 = ram[ ram[ip+2] ]
    # third param; arg3 is the writing location of opcodes 1,2,7,8
    if modes[2] == 2:
        arg3 = ram[ip+3] + base
    else:
        arg3 = ram[ip+3]
    return arg1, arg2, arg3


def processor ( ram, ip, base_addr ):
    opcode, modes = parse_opcode( ram[ip] )
    arg1, arg2, arg3 = modal_parameters( opcode, ram, ip, modes, base_addr )
    if   opcode == 1:
        ram[ arg3 ] = arg1 + arg2
        return ram, ip+4, base_addr
    elif opcode == 2:
        ram[ arg3 ] = arg1 * arg2
        return ram, ip+4, base_addr
    elif opcode == 3:
        id_input = int( input( "\n Please provide a program input: " ) )
        ram[ arg1 ] = id_input
        return ram, ip+2, base_addr
    elif opcode == 4:
        print( "\n The program has outputted: %d\n\n" % arg1 )
        return ram, ip+2, base_addr
    elif opcode == 5:
        if arg1:
            return ram, arg2, base_addr
        else:
            return ram, ip+3, base_addr
    elif opcode == 6:
        if not arg1:
            return ram, arg2, base_addr
        else:
            return ram, ip+3, base_addr
    elif opcode == 7:
        if arg1 < arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4, base_addr
    elif opcode == 8:
        if arg1 == arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4, base_addr
    elif opcode == 9:
        return ram, ip+2, base_addr + arg1
    elif opcode == 99:
        return ram, -1, base_addr
    else:
        print("unknown opcode")



##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

program = [ int( s ) for s in line.rstrip().split(sep=",") ]

print( "\n read %d Intcodes from input file\n" % ( len(program) ) )

# "The computer's available memory should be much larger than the initial program."
#   numpy arrays are faster that linked lists
ram_array = np.asarray( program )
padding = np.zeros( 900000000, dtype=int )
ram_array = np.append( ram_array, padding )

ip = 0
base_addr = 0
while ip != -1:
    ram_array, ip, base_addr = processor( ram_array, ip, base_addr )

