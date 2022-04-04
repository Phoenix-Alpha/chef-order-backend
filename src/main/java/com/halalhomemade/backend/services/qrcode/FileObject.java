package com.halalhomemade.backend.services.qrcode;

import java.io.InputStream;
import lombok.Getter;

@Getter
public class FileObject {
  private String fileName;
  private InputStream fileStream;

  public FileObject(String fileName, InputStream fileStream) {
    super();
    this.fileName = fileName;
    this.fileStream = fileStream;
  }
}
