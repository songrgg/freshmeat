package me.songrgg.freshmeat.helper;

import org.jsoup.nodes.Document;

/**
 * @author songrgg
 * @since 1.0
 */
public class HtmlParser {
  private Document document;

  public void setContent(String content) {
    this.document = org.jsoup.Jsoup.parse(content);
  }

  public String text() {
    return document.text();
  }
}
