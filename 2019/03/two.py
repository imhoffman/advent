#!/usr/bin/env python3

##
##  subprograms
##

#  parse directions into ordered pairs of trajectory points
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


#  routine for finding intersections among trajectories
def intersector ( wire1, wire2 ):
    intersections = []
    for i in range( len( wire1 )-1 ):
        if ( wire1[i][0] == wire1[i+1][0] ):    # the x coords are the same
            ymin = wire1[i][1]
            ymax = wire1[i+1][1]
            if ymin > ymax:
                ymax, ymin = ymin, ymax
            for j in range( len( wire2 )-1 ):
                if ( wire2[j][1] == wire2[j+1][1] ):  # the y coords are the same
                    xmin = wire2[j][0]
                    xmax = wire2[j+1][0]
                    if xmin > xmax:
                        xmax, xmin = xmin, xmax
                    if wire2[j][1] >= ymin and wire2[j][1] <= ymax \
                            and wire1[i][0] >= xmin and wire1[i][0] <= xmax:
                                intersections.append( [ wire1[i][0], wire2[j][1] ] )
        if ( wire1[i][1] == wire1[i+1][1] ):    # the y coords are the same
            xmin = wire1[i][0]
            xmax = wire1[i+1][0]
            if xmin > xmax:
                xmax, xmin = xmin, xmax
            for j in range( len( wire2 )-1 ):
                if ( wire2[j][0] == wire2[j+1][0] ):  # the x coords are the same
                    ymin = wire2[j][1]
                    ymax = wire2[j+1][1]
                    if ymin > ymax:
                        ymax, ymin = ymin, ymax
                    if wire2[j][0] >= xmin and wire2[j][0] <= xmax \
                            and wire1[i][1] >= ymin and wire1[i][1] <= ymax:
                                intersections.append( [ wire2[j][0], wire1[i][1] ] )
    return intersections


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

intersections = intersector( wire1_path, wire2_path )
temp_min = 100000000
for pair in intersections:
    manhattan_distance = abs(pair[0]) + abs(pair[1])
    if manhattan_distance < temp_min:
        temp_min = manhattan_distance

print( "\n distance to closest intersection: %d\n\n" % (temp_min) )





