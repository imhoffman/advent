#!/usr/bin/env python3

##
##  subprograms
##

def parser ( wire_routing, file_name ):
    x, y = 0, 0
    with open( file_name, "w" ) as output_file:
        output_file.write( "%d %d\n" % ( x, y ) )
        for directive in wire_routing:
            if directive[0] == 'D':
                y = y - int( directive[1:] )
                output_file.write( "%d %d\n" % ( x, y ) )
            elif directive[0] == 'U':
                y = y + int( directive[1:] )
                output_file.write( "%d %d\n" % ( x, y ) )
            elif directive[0] == 'L':
                x = x - int( directive[1:] )
                output_file.write( "%d %d\n" % ( x, y ) )
            elif directive[0] == 'R':
                x = x + int( directive[1:] )
                output_file.write( "%d %d\n" % ( x, y ) )


##
##  main program
##
with open("puzzle.txt") as fo:
  wire1 = fo.readline()
  wire2 = fo.readline()

wire1 = [ s for s in wire1.rstrip().split(sep=",") ]
wire2 = [ s for s in wire2.rstrip().split(sep=",") ]

parser( wire1, "plot1.dat" )
parser( wire2, "plot2.dat" )

