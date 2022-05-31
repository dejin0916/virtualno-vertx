package com.lee.virtualno.apiservice.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptReader {
  public static String getXForAx() throws IOException {
    return read("get_x_for_ax.lua");
  }

  public static String getXForAxb() throws IOException {
    return read("get_x_for_axb.lua");
  }

  private static String read(String file) throws IOException {
    Path path = Paths.get("api-service/src/main/resources/lua", file);
    if (!path.toFile().exists()) {
      path = Paths.get("..", file);
    }
    return String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
  }
}
