
io.input("puzzle.txt")
input = {}
for line in io.lines() do
  local captures = { line:match( "(%d+)%-(%d+) (%w+): (%w+).*" ) }
  table.insert( input, { tonumber( captures[1] ),
                         tonumber( captures[2] ),
			 captures[3], captures[4] } )
end


function string_to_table ( s )
  local t = {}
  for i = 1, #s do
    t[i] = s:sub(i,i)
  end
  return t
end


nvalid = 0
for i = 1, #input do
  local t = input[i]
  local i1, i2, c, s = t[1], t[2], t[3], t[4]
  local st = string_to_table( s )
  local b1 = c==st[i1]
  local b2 = c==st[i2]
  if ( b1 ~= b2 ) then
    nvalid = nvalid + 1
  end
end

print(nvalid)

