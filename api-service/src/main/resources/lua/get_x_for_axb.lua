local function table_diff(tbl_a, tbl_b)
  if(type(tbl_a) ~= 'table' or next(tbl_a) == nil) then
    return nil
  end
  if(type(tbl_b) ~= 'table' or next(tbl_b) == nil) then
    return tbl_a;
  end
  local tbl_c = {}
  for _, v in pairs(tbl_a) do
    for _, vv in pairs(tbl_b) do
      if(v == vv) then
        v = nil
        break
      end
    end
    if v ~= nil then
      table.insert(tbl_c, v)
    end
  end
  return tbl_c
end

local function split(str, reps)
  local rs = {}
  string.gsub(str,'[^'..reps..']+', function(w)
    table.insert(rs, w)
  end)
end

local excludes, sn = cjson.decode(ARGV[1]), ARGV[3]
local rangeLimit = table.getn(excludes) + 1
local vn
local dialLimit = 500
local bindLimit = 500

local tblKeys={}
table.insert(tblKeys, 1, KEYS[1].."_"..dialLimit)
table.insert(tblKeys, 2, KEYS[2].."_"..bindLimit)
if nil ~= KEY[3] then
  table.insert(tblKeys, 3, KEYS[3].."_"..dialLimit)
  table.insert(tblKeys, 4, KEYS[4].."_"..bindLimit)
end

for _, v in pairs(tblKeys) do
  if vn == nil then
    local tb_s = split(v, "_")
    local k,c = tb_s[1],tonumber(tb_s[2])
    vn = redis.call("ZRANGEBYSCORE", k, 0, c - 1, 'LIMIT', 0, rangeLimit)
    vn = table_diff(vn, excludes)
    if vn ~= nil and vn[1] ~= nil then
      redis.call("ZINCRBY", string.format("VN:POOL:{%s}:BIND:TIMES", sn), 1, vn[1])
      local area = redis.call('HGET', string.format("VN:POOL:{%s}:VN:AREA", sn), vn[1])
      if area ~= nill and type(area) ~= 'boolean' then
        redis.call("ZINCRBY", string.format("VN:POOL:{%s}:%s:BIND:TIMES", sn, area), 1, vn[1])
      end
    end
  end
end
