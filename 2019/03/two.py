#!/usr/bin/env python3

##
##  subprograms
##

#  parse directions into ordered pairs of trajectory points
def parser ( wire_routing ):
    x, y, total_path = 0, 0, 0
    points = []
    points.append( [x,y,total_path] )
    for directive in wire_routing:
        delta = int( directive[1:] ) 
        if directive[0] == 'D':
            y = y - delta
            total_path = total_path + delta
        elif directive[0] == 'U':
            y = y + delta
            total_path = total_path + delta
        elif directive[0] == 'L':
            x = x - delta
            total_path = total_path + delta
        elif directive[0] == 'R':
            x = x + delta
            total_path = total_path + delta
        points.append( [x,y,total_path] )
    return points



#  routine for finding intersections among trajectories
#  and the combined number of coords traversed to the intersection
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
                                intersections.append( [ wire1[i][0], wire2[j][1], \
                                    wire1[i][2]+wire2[j][2]+(wire2[j][1]-ymin)+(wire1[i][0]-xmin) ] )
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
                                intersections.append( [ wire2[j][0], wire1[i][1], \
                                        wire1[i][2]+wire2[j][2]+(wire2[j][0]-xmin)+(wire1[i][1]-ymin) ] )
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

#  part one
temp_min = 100000000
for pair in intersections:
    manhattan_distance = abs(pair[0]) + abs(pair[1])
    if manhattan_distance < temp_min:
        temp_min = manhattan_distance

print( "\n distance to closest intersection: %d\n\n" % (temp_min) )

#  path two
temp_min = 100000000
for entry in intersections:
    if entry[2] < temp_min:
        temp_min = entry[2]

print( "\n shortest delay to an intersection: %d\n\n" % (temp_min) )





