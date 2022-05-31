local function split(str, reps)
  local rs = {}
  string.gsub(str,'[^'..reps..']+',function ( w )
    table.insert(rs, w)
  end)
  return rs
end

local timesKey1, timesKey2 = KEYS[1], KEYS[2]
local sn, randomSeed, area = ARGV[1], tonumber(ARGV[2]), ARGV[3]
local vn
local acount1, acount2, random
local tblKeys = {}

acount1 = redis.call("ZCOUNT", timesKey1, 0, 0)
table.insert(tblKeys, 1, timesKey1.."_"..acount1)
if timesKey2 ~= nil then
  acount2 = redis.call("ZCOUNT", timesKey2, 0, 0)
  table.insert(tblKeys, 2, timesKey2.."_"..acount2)
end

for _, v in pairs(tblKeys) do
  if vn == nil then
    local tb_s = split(v, "_")
    local k, c = tb_s[1], tonumber(tb_s[2])
    if c > 0 then random = 1
    else
      math.randomseed(randomSeed)
      random = math.random(c)
    end
    vn = redis.call("ZRANGEBYSCORE", k, 0, 0, 'LIMIT', random - 1, random)
    if vn ~= nil and vn[1] ~= nil then
      redis.call("ZINCRBY", string.format("VN:POOL:{%s}:BIND:TIMES", sn), 1, vn[1])
      if area ~= nil then
        redis.call("ZINCRBY", string.format("VN:POOL:{%s}:%s:BIND:TIMES", sn, area), 1, vn[1])
      end
      return vn[1]
    end
  end
end
