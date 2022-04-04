package com.halalhomemade.backend.utils;

import com.google.common.net.UrlEscapers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlUtils {

  public static boolean isUrlValid(String url) {
    return new UrlValidator(new String[] {"http", "https"})
        .isValid(UrlUtils.addSchemeIfMissing(url));
  }

  public static String addSchemeIfMissing(String url) {
    if (!(StringUtils.startsWithIgnoreCase(url, "http://")
        || StringUtils.startsWithIgnoreCase(url, "https://"))) {
      return StringUtils.join("https://", url);
    }
    return url;
  }

  public static String encodeFilename(final String filename) {
    return UrlEscapers.urlPathSegmentEscaper().escape(filename);
  }
}
