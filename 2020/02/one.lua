
io.input("puzzle.txt")
input = {}
for line in io.lines() do
  local captures = { line:match( "(%d+)%-(%d+) (%w+): (%w+).*" ) }
  table.insert( input, { tonumber( captures[1] ),
                         tonumber( captures[2] ),
			 captures[3], captures[4] } )
end

--[[
for i = 1, #input do
  print( input[i][2] )
end
--]]

--[[
function print_table (t)
  for k,v in pairs(t) do
    print(k,v)
  end
end
--]]

function frequencies ( s, accum )
  local c = s:match("(%a)") 
  if c then
    local g = {s:gsub(c,0)}
    accum[c] = g[2]
    frequencies( g[1], accum )
  end
  return accum
end


nvalid = 0
for i = 1, #input do
  local t = input[i]
  local nmin, nmax, c, s = t[1], t[2], t[3], t[4]
  local ccount = frequencies( s, {} )[c]
  if ( ccount and ccount >= nmin and ccount <= nmax ) then
    nvalid = nvalid + 1
  end
end

print(nvalid)

