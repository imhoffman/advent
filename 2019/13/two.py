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


class ArcadeCabinet(object):
    def __init__( self, program, width, length ):
        self.ram = program
        self.ip = 0
        self.base_addr = 0
        self.blocks = 0
        self.number_of_outputs = 0
        self.array_of_outputs = [-1,-1,-1]     # for testing
        self.width = width
        self.length = length
        self.screen_state = [ [ 0 for _ in range(width) ] for _ in range(length) ]
        self.cheat = False
        self.cheat_count = 0
        self.cheat_limit = 5000
        return


    def input_to_program ( self ):
        if self.cheat and self.cheat_count < self.cheat_limit:
            joystick = 0
            self.cheat_count += 1
        else:
            self.cheat_count = 0
            self.cheat = False
            joystick = int( input( "Joystick (-1, 0, 1): " ) )
            if joystick in ( -1, 0, 1 ):
                joystick = joystick
            elif joystick >= self.length:
                self.cheat_limit = joystick
                self.cheat = True
                joystick = 0
            elif joystick < -1 and self.screen_state[ joystick ][ 5 ] == 1:
                self.screen_state[ joystick ][ 5 ] = 0
                joystick = 0
            elif joystick > 1 and joystick < self.length and self.screen_state[ joystick ][ 5 ] == 0:
                self.screen_state[ joystick ][ 5 ] = 1
                joystick = 0
            else:
                joystick = 0
        return joystick


    def output_from_program ( self, value ):
        if self.number_of_outputs == 0:
            self.array_of_outputs[0] = value
            self.number_of_outputs = 1
            return
        elif self.number_of_outputs == 1:
            self.array_of_outputs[1] = value
            self.number_of_outputs = 2
            return
        elif self.number_of_outputs == 2:
            self.array_of_outputs[2] = value
            if value == 2:
                self.blocks += 1
            if (-1,0) == ( self.array_of_outputs[0], self.array_of_outputs[1] ):
                print( " current score: %d\n" % value )
            else:
                self.screen_state[ self.array_of_outputs[0] ][ self.array_of_outputs[1] ] = value
                self.render()
            self.number_of_outputs = 0
            self.array_of_outputs = [-1,-1,-1]
            return
        else:
            print( " problem with output handler\n" )
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
            joystick = self.input_to_program()
            ram[ arg1 ] = joystick
            return ram, ip+2, base_addr
        elif opcode == 4:
            #print( " output: %d\n" % arg1 )
            self.output_from_program( arg1 )
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


    def render ( self ):
        length = self.length
        width = self.width
        state = self.screen_state
        for i in range( length ):
            for j in range( width ):
                if state[i][j] == 0:
                    print( ' ', end='', flush=True )
                elif state[i][j] == 1:
                    print( '#', end='', flush=True )
                elif state[i][j] == 2:
                    print( '\033[36m8\033[0m', end='', flush=True )
                elif state[i][j] == 3:
                    print( '[', end='', flush=True )
                elif state[i][j] == 4:
                    print( '\033[31m\033[1m\033[43mo\033[0m', end='', flush=True )
            print()
        print()

##  end of robot class



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
padding = np.zeros( 900000, dtype=int )
ram_array = np.append( ram_array, padding )

#  guess at screen size based on output integers
cabinet = ArcadeCabinet( ram_array, 24, 44 )
cabinet.execute()

