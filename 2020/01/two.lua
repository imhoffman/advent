
io.input("puzzle.txt")
input = {}
for line in io.lines() do
  table.insert( input, tonumber( line:match( "(%d+).*" ) ) )
end


for i = 1, #input do
  local a = input[i]
  for j = i, #input do
    local b = input[j]
    if ( a + b == 2020 ) then print( " part one:", a*b ) end
    for k = j, #input do
      local c = input[k]
      if ( a+b+c == 2020 ) then print( " part two:", a*b*c ) end
    end
  end
end

