#!/usr/bin/env python3

##
##  subprograms
##

def parse_opcode ( ABCDE ):
    as_char_array = str( ABCDE )
    opcode = int( as_char_array[-1:] )
    if opcode == 9 and int( as_char_array[-2:-1] ) == 9:
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
def modal_parameters ( ram, ip, modes, base ):
    # first param
    if modes[0] == 1:
        arg1 = ram[ip+1]
    elif modes[0] == 2:
        arg1 = ram[ ram[ip+1] ] + base
    else:
        arg1 = ram[ ram[ip+1] ]
    # second param
    if modes[1] == 1:
        arg2 = ram[ip+2]
    elif modes[1] == 2:
        arg2 = ram[ ram[ip+2] ] + base
    else:
        arg2 = ram[ ram[ip+2] ]
    # "Parameters that an instruction writes to will never be in immediate mode."
    if modes[2] == 2:
        arg3 = ram[ip+3] + base
    else:
        arg3 = ram[ip+3]
    return arg1, arg2, arg3


def processor ( ram, ip, base ):
    opcode, mode1, mode2, mode3 = parse_opcode( ram[ip] )
    if opcode in ( 1, 2, 5, 6, 7, 8, 9 ):
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3), base )
    if   opcode == 1:
        ram[ arg3 ] = arg1 + arg2
        return ram, ip+4, base
    elif opcode == 2:
        ram[ arg3 ] = arg1 * arg2
        return ram, ip+4, base
    elif opcode == 3:
        id_input = int( input( "\n Please provide a program input: " ) )
        ram[ ram[ip+1] ] = id_input
        return ram, ip+2, base
    elif opcode == 4:
        print( "\n The program has outputted: %d\n\n" % ram[ ram[ip+1] ] )
        return ram, ip+2, base
    elif opcode == 5:
        if arg1:
            return ram, arg2, base
        else:
            return ram, ip+3, base
    elif opcode == 6:
        if not arg1:
            return ram, arg2, base
        else:
            return ram, ip+3, base
    elif opcode == 7:
        if arg1 < arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4, base
    elif opcode == 8:
        if arg1 == arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4, base
    elif opcode == 9:
        return ram, ip+3, base+arg1
    elif opcode == 99:
        return ram, -1, base
    else:
        print("unknown opcode")



##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

program = [ int( s ) for s in line.rstrip().split(sep=",") ]

print( "\n read %d commands from input file\n" % ( len(program) ) )

# "The computer's available memory should be much larger than the initial program."
for _ in range(8192):
    program.append(0)

ip = 0
base = 0
while ip != -1:
    program, ip, base = processor( program, ip, base )

