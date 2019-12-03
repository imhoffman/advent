#!/usr/bin/env python3

##
##  subprograms
##

def parser ( wire_routing ):
    x, y = 0, 0
    points = []
    points.append( [x,y] )
    for directive in wire_routing:
        if directive[0] == 'D':
            y = y - int( directive[1:] )
        elif directive[0] == 'U':
            y = y + int( directive[1:] )
        elif directive[0] == 'L':
            x = x - int( directive[1:] )
        elif directive[0] == 'R':
            x = x + int( directive[1:] )
        points.append( [x,y] )
    return points


##
##  main program
##
with open("puzzle.txt") as fo:
  wire1 = fo.readline()
  wire2 = fo.readline()

wire1 = [ s for s in wire1.rstrip().split(sep=",") ]
wire2 = [ s for s in wire2.rstrip().split(sep=",") ]

wire1_path = parser( wire1 )
wire2_path = parser( wire2 )

print( wire1_path )

