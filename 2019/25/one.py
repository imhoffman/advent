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


class Droid(object):
    def __init__( self, program, width, length ):
        self.ram = program
        self.ip = 0
        self.base_addr = 0
        self.output_list = []
        return


    def processor ( self, ram, ip, base_addr ):
        ram = self.ram
        ip = self.ip
        base_addr = self.base_addr
        opcode, modes = parse_opcode( ram[ip] )
        arg1, arg2, arg3 = modal_parameters( opcode, ram, ip, modes, base_addr )
        if   opcode == 1:
            ram[ arg3 ] = arg1 + arg2
            return ram, ip+4, base_addr
        elif opcode == 2:
            ram[ arg3 ] = arg1 * arg2
            return ram, ip+4, base_addr
        elif opcode == 3:
            input_command = input( " Input: " )
            ram[ arg1 ] = input_command
            return ram, ip+2, base_addr
        elif opcode == 4:
            self.output_list.append( arg1 )
            if arg1 == 10:
                self.output_report()
            if arg1 > 127:
                print( " non-ASCII output:", arg1 )
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
        return


    def execute ( self ):
        while self.ip != -1:
            self.ram, self.ip, self.base_addr = \
                    self.processor( self.ram, self.ip, self.base_addr )


    def output_report ( self ):
        print( ''.join( [ chr(c) for c in self.output_list ] ) )
        self.output_list.clear()
        return

##  end of droid class



##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

program = [ int( s ) for s in line.rstrip().split(sep=",") ]

#print( "\n read %d Intcodes from input file\n" % ( len(program) ) )

# "The computer's available memory should be much larger than the initial program."
#   numpy arrays are faster that linked lists
ram_array = np.asarray( program )
padding = np.zeros( 9000000, dtype=int )
ram_array = np.append( ram_array, padding )

#  map size seems unecessary --- enter `width`, `length`
droid = Droid( ram_array, 18, 40 )
droid.execute()
droid.output_report()


