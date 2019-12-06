#!/usr/bin/env python3

##
##  subprograms
##

def parse_opcode ( ABCDE ):
    as_char_array = str( ABCDE )
    opcode = int( as_char_array[-1:] )
    if opcode == 9:
        opcode = 99
    if as_char_array[-3:-2]:
        C = int( as_char_array[-3:-2] )
    else:
        C = 0
    if as_char_array[-4:-3]:
        B = int( as_char_array[-4:-3] )
    else:
        B = 0
    if as_char_array[-5:-4]:
        A = int( as_char_array[-5:-4] )
    else:
        A = 0
    return opcode, C, B, A

#  for fetching the appropriate value as per the instruction modalities
def modal_parameters ( ram, ip, modes ):
        if modes[0]:
            arg1 = ram[ip+1]
        else:
            arg1 = ram[ ram[ip+1] ]
        if modes[1]:
            arg2 = ram[ip+2]
        else:
            arg2 = ram[ ram[ip+2] ]
        # "Parameters that an instruction writes to will never be in immediate mode."
        assert modes[2] == 0
        arg3 = ram[ip+3]
        return arg1, arg2, arg3


def processor ( ram, ip ):
    opcode, mode1, mode2, mode3 = parse_opcode( ram[ip] )
    #print( " ip:", ip, " opcode:", ram[ip], "  parsed opcode:", opcode, " modes:", mode1,mode2,mode3 )
    if   opcode == 1:
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
        ram[ arg3 ] = arg1 + arg2
        return ram, ip+4
    elif opcode == 2:
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
        ram[ arg3 ] = arg1 * arg2
        return ram, ip+4
    elif opcode == 3:
        id_input = int( input( "\n Please enter the ID of the system to test: " ) )
        ram[ ram[ip+1] ] = id_input
        return ram, ip+2
    elif opcode == 4:
        print( "\n The program has outputted: %d\n\n" % ram[ ram[ip+1] ] )
        return ram, ip+2
    elif opcode == 5:
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
        if arg1:
            return ram, arg2
        else:
            return ram, ip+3
    elif opcode == 6:
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
        if not arg1:
            return ram, arg2
        else:
            return ram, ip+3
    elif opcode == 7:
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
        if arg1 < arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4
    elif opcode == 8:
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
        if arg1 == arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4
    elif opcode == 99:
        return ram, -1         # catch -1 in main and halt
    else:
        print("unknown opcode")



##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

program = [ int( s ) for s in line.rstrip().split(sep=",") ]

print( "\n read %d commands from input file\n" % ( len(program) ) )

ip = 0                              # aka program counter
while ip != -1:
    program, ip = processor( program, ip)

print( "\n the value at position 0 after halting is %d\n\n" % program[0] )

