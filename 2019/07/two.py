#!/usr/bin/env python3

from math import factorial
from itertools import permutations

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


def processor ( ram, ip, input_value ):
    opcode, mode1, mode2, mode3 = parse_opcode( ram[ip] )
    output_value = -1   # default non-opcode-4 return value ... is this safe?
    if opcode in ( 1, 2, 5, 6, 7, 8 ):
        arg1, arg2, arg3 = modal_parameters( ram, ip, (mode1, mode2, mode3) )
    if   opcode == 1:
        ram[ arg3 ] = arg1 + arg2
        return ram, ip+4, output_value
    elif opcode == 2:
        ram[ arg3 ] = arg1 * arg2
        return ram, ip+4, output_value
    elif opcode == 3:
        ram[ ram[ip+1] ] = input_value
        return ram, ip+2, output_value
    elif opcode == 4:
        output_value = ( ram[ ram[ip+1] ] )
        return ram, ip+2, output_value
    elif opcode == 5:
        if arg1:
            return ram, arg2, output_value
        else:
            return ram, ip+3, output_value
    elif opcode == 6:
        if not arg1:
            return ram, arg2, output_value
        else:
            return ram, ip+3, output_value
    elif opcode == 7:
        if arg1 < arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4, output_value
    elif opcode == 8:
        if arg1 == arg2:
            ram[ arg3 ] = 1
        else:
            ram[ arg3 ] = 0
        return ram, ip+4, output_value
    elif opcode == 99:
        return ram, -1, output_value         # catch -1 in main and halt
    else:
        print("unknown opcode")



#  the state of any one amplifier, including its ram
def class amplifier:
    def __init__( self, program, phase_setting ):
        self.program = program
        self.phase = phase_setting
        self.input_value = 0
        self.original_program = []
        self.original_program[:] = program[:]

    def obtain_input( self, new_input ):
        self.input_value = new_input
        return

    def generate_output( self ):
        # "Don't restart the Amplifier Controller Software on any amplifier during this process" ... but that can't mean don't reset the program counter
        ip = 0
        # "memory is not shared or reused between copies of the program" ... but maybe I shouldn't reload the program into ram (?)
        #self.program[:] = self.original_program[:]
        output_value = -1            # processor returns -1 when not opcode 4
        while output_value == -1:
            self.program, ip, output_value = processor( self.program, ip, self.input_value )
        return output_value




def thrusters( program, phase_settings ):

    # "memory is not shared or reused between copies of the program"
    Amp_A = amplifier( program, phase_settings[0] )
    Amp_B = amplifier( program, phase_settings[1] )
    Amp_C = amplifier( program, phase_settings[2] )
    Amp_D = amplifier( program, phase_settings[3] )
    Amp_E = amplifier( program, phase_settings[4] )

    ip = 0
    for _ in range(5):   # this needs to change
        if ip == -1:
            ip = 0
            program[:] = original_program[:]
        while ip != -1:
            program, ip = processor( program, ip, amp_chain )

    return amp_chain.thruster_output()


#  https://docs.python.org/3.8/library/itertools.html#itertools.permutations
def search_phase_settings( program ):
    temp_max = 0
    for p in permutations( (9,8,7,6,5) ):
        trial = thrusters( program, p )
        if trial > temp_max:
            temp_max = trial
            best_config = p
    return temp_max, best_config



##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

program = [ int( s ) for s in line.rstrip().split(sep=",") ]

print( "\n read %d commands from input file\n" % ( len(program) ) )

answer, config = search_phase_settings( program ) 
print( "\n For phases", config, "maximal thruster output", answer, "is obtained.\n\n" )

