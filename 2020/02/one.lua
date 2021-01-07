
io.input("puzzle.txt")
input = {}
for line in io.lines() do
  local captures = { line:match( "(%d+)%-(%d+) (%w+): (%w+).*" ) }
  table.insert( input, { tonumber( captures[1] ),
                         tonumber( captures[2] ),
			 captures[3], captures[4] } )
end


for i = 1, #input do
  print( input[i][2] )
end

