#!/usr/bin/env python3

from math import factorial

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


def processor ( ram, ip, amp_config ):
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
        #user_input = int( input( "\n Please provide input: " ) )
        user_input = amp_config.provide_input()
        ram[ ram[ip+1] ] = user_input
        return ram, ip+2
    elif opcode == 4:
        #print( "\n The program has outputted: %d\n\n" % ram[ ram[ip+1] ] )
        amp_config.receive_output( ram[ ram[ip+1] ] )
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


class amplifiers:
  def __init__(self, phase_settings):
    self.settings = phase_settings
    self.number_of_calls = 0
    self.latest_output = 0

  def provide_input ( self ):
    self.number_of_calls += 1
    d = self.number_of_calls
    if d % 2 == 0:
        f = self.latest_output
    else:
        f = self.settings[ int( (d-1)/2 ) ]
    print( " providing %d for the %dth input\n" % ( f, d ) )
    return f

  def receive_output ( self, n ):
    self.latest_output = n
    return

  def thruster_output ( self ):
    return self.latest_output


def thrusters( program, phase_settings ):
    original_program = []
    original_program[:] = program[:]

    amp_object = amplifiers( phase_settings )

    ip = 0
    for _ in range(5):
        if ip == -1:
            ip = 0
            program[:] = original_program[:]
        while ip != -1:
            program, ip = processor( program, ip, amp_object )

    return amp_object.thruster_output()



##
##  main program
##
with open("puzzle.txt") as fo:
  line = fo.readline()

program = [ int( s ) for s in line.rstrip().split(sep=",") ]

print( "\n read %d commands from input file\n" % ( len(program) ) )

phase_settings = ( 1, 0, 4, 3, 2 )
print( "\n Thruster output: %d\n\n" % thrusters( program, phase_settings) )

