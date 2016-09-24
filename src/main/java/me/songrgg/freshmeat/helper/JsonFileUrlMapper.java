package me.songrgg.freshmeat.helper;

/**
 * @author songrgg
 * @since 1.0
 */

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonFileUrlMapper extends UrlMapper {

  public JsonFileUrlMapper(String path) throws IOException {
    super(jsonToMap(readFile(path, StandardCharsets.UTF_8)));
  }

  static String readFile(String path, Charset encoding)
          throws IOException {
    ClassPathResource resource = new ClassPathResource(path);
    return convertStreamToString(resource.getInputStream());
  }

  static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  private static Map<String, String> jsonToMap(String t) throws JSONException {

    HashMap<String, String> map = new HashMap<>();
    JSONObject jObject = new JSONObject(t);
    Iterator<?> keys = jObject.keys();

    while (keys.hasNext()) {
      String key = (String) keys.next();
      String value = jObject.getString(key);
      map.put(key, value);
    }
    return map;
  }
}
