function utf8EncodeChars(string) {
  string = `${string}`;
  let encoded = [];
  for (let i in string) {
    encoded.push(string.charCodeAt(i));
  }
  return encoded;
}

konan.libraries.push({
  kotlinx__currentTimeMillis: function(arena) {
    const timestamp = new Date().getTime();
    return toArena(arena, utf8EncodeChars(timestamp));
  },
});
