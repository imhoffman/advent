#!/usr/bin/env python3

import numpy as np
import random, os, sys
random.seed( os.urandom( 128 ) )

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


class RepairDroid(object):
    def __init__( self, program, width, length ):
        self.ram = program
        self.ip = 0
        self.base_addr = 0
        # start somewhere that the location index won't go negative
        self.width = width
        self.length = length
        self.position = [ int( self.width/2 ),int( self.length/2) ]
        self.orig_position = []
        self.orig_position[:] = self.position[:]
        self.requested_position = []
        self.requested_position[:] = self.position[:]      # OMG, burned again by the pointer !!!!!
        #   -1 for unexplored
        self.section_map = [ [ -1 for _ in range(length) ] for _ in range(width) ]
        self.section_map[ self.position[0] ][ self.position[1] ] = 3
        self.visited = set()
        self.visited.add( ( self.position[0], self.position[1] ) )
        self.system_status = -1       # -1 for testing
        self.user_input = -1          # -1 for testing
        return


    def input_to_program ( self ):
        self.user_input = random.randint(1,4)
        #minecraft = input( " Movement instruction for the droid: " )
        #if minecraft == 'a':
        #    self.user_input = 3
        #elif minecraft == 'd':
        #    self.user_input = 4
        #elif minecraft == 's':
        #    self.user_input = 2
        #elif minecraft == 'w':
        #    self.user_input = 1
        #else:
        #    print( " invalid input\n" )
        #    return self.input_to_program()
        self.move_robot( self.user_input, False )
        return self.user_input


    #  like Arkanoid:
    #   0 for empty, 1 for wall, 3 for player, default -1 for unexplored
    def output_from_program ( self, value ):
        if value == 0:
            self.section_map[ self.requested_position[0] ][ self.requested_position[1] ] = 1
        elif value == 1:
            self.move_robot( self.user_input, True )
        elif value == 2:
            self.move_robot( self.user_input, True )
            self.section_map[ self.position[0] ][ self.position[1] ] = 2
            self.oxygen_location = ( self.position[0], self.position[1] )
            #print( "Found oxygen system at: ", self.position[0], self.position[1] )
            #print( " number of unique locations visited: %d\n" % ( len( self.visited) ) )
            self.final_render()
        else:
            print( " problem processing output\n" )
        #self.render()
        return
            

    def move_robot ( self, move, actual_move ):
        self.requested_position[:] = self.position[:]
        if move == 1:
            if actual_move:
                # set old position to explored but empty
                self.section_map[ self.position[0] ][ self.position[1] ] = 0
                # update position appropriately
                self.position[1] -= 1
            else:
                self.requested_position[1] -= 1
        elif move == 2:
            if actual_move:
                self.section_map[ self.position[0] ][ self.position[1] ] = 0
                self.position[1] += 1
            else:
                self.requested_position[1] += 1
        elif move == 3:
            if actual_move:
                self.section_map[ self.position[0] ][ self.position[1] ] = 0
                self.position[0] -= 1
            else:
                self.requested_position[0] -= 1
        elif move == 4:
            if actual_move:
                self.section_map[ self.position[0] ][ self.position[1] ] = 0
                self.position[0] += 1
            else:
                self.requested_position[0] += 1
        else:
            print( " problem with move instructions\n" )
        # set new position to robot
        self.section_map[ self.position[0] ][ self.position[1] ] = 3
        self.visited.add( ( self.position[0], self.position[1] ) )
        self.section_map[ self.orig_position[0] ][ self.orig_position[1] ] = 4
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
            input_command = self.input_to_program()
            ram[ arg1 ] = input_command
            return ram, ip+2, base_addr
        elif opcode == 4:
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
       state = self.section_map
       for j in range( length ):
           for i in range( width ):
               if state[i][j] == -1 :
                   print( '-', end='', flush=True )
               elif state[i][j] == 0 :
                   print( ' ', end='', flush=True )
               elif state[i][j] == 1 :
                   print( '#', end='', flush=True )
               elif state[i][j] == 3 :
                   print( '\033[31m\033[1m\033[43m+\033[0m', end='', flush=True )
               elif state[i][j] == 2 :
                   print( '\033[36mX\033[0m', end='', flush=True )
               else:
                   print( ' ', end='', flush=True )
           print()
       print()
       return


    def final_render ( self ):
       length = self.length
       width = self.width
       Dx = abs( width - self.oxygen_location[0] )
       Dy = abs( length - self.oxygen_location[1] )
       state = self.section_map
       for j in range( length ):
           for i in range( width ):
               if state[i][j] == -1 :
                   print( '-', end='', flush=True )
               elif state[i][j] == 0 :
                   print( ' ', end='', flush=True )
               elif state[i][j] == 1 :
                   print( '#', end='', flush=True )
               elif state[i][j] == 3 :
                   print( '\033[31m\033[1m\033[43m+\033[0m', end='', flush=True )
               elif state[i][j] == 2 :
                   print( '\033[36mX\033[0m', end='', flush=True )
               elif state[i][j] == 4 :
                   print( '\033[31mX\033[0m', end='', flush=True )
               else:
                   print( ' ', end='', flush=True )
           print()
       print()
       sys.exit(0)
       return

##  end of robot class



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
padding = np.zeros( 900000, dtype=int )
ram_array = np.append( ram_array, padding )

#  guess at necessary map size --- enter `width`, `length`
droid = RepairDroid( ram_array, 100, 80 )
#droid.render()
droid.execute()

# Found oxygen system at:  4108 4110 when 8192,8192

