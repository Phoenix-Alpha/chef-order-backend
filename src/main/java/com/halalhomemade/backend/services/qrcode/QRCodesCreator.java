//package com.halalhomemade.backend.services.qrcode;
//
//import com.halalhomemade.backend.exceptions.InternalServiceException;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import java.awt.AlphaComposite;
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//import java.util.EnumMap;
//import java.util.Map;
//import javax.imageio.ImageIO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class QRCodesCreator {
//
//  @Value("${qrcode.size:320}")
//  private int qrCodeSize;
//
//  @Value("${qrcode.color:#000000}")
//  private Color qrCodeColor;
//
//  /**
//   * Resizes the image
//   *
//   * @param image The image to resize
//   * @param newWidth The new width
//   * @param newHeight The new height
//   * @return The resized image
//   */
//  private BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
//
//    BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
//    Graphics2D graphics = resized.createGraphics();
//    graphics.drawImage(
//        image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
//    graphics.dispose();
//
//    return resized;
//  }
//
//  /**
//   * Gets the hint map to be used while QR code generation
//   *
//   * @return
//   */
//  private Map<EncodeHintType, Object> getHintMap() {
//
//    Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
//    hintMap.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
//    hintMap.put(EncodeHintType.MARGIN, 1);
//    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//    return hintMap;
//  }
//
//  /**
//   * Gets the QR code bit matrix
//   *
//   * @param data The data
//   * @param hintMap The hint map
//   * @return The bit matrix
//   * @throws WriterException The writer exception, thrown when bit matrix generation fails
//   */
//  private BitMatrix getQRBitMatrix(String data, Map<EncodeHintType, Object> hintMap)
//      throws WriterException {
//
//    return new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
//  }
//
//  /**
//   * Generates QR image from bit matrix
//   *
//   * @param bitMatrix The bit matrix
//   * @return The QR image
//   */
//  private BufferedImage generateQRImage(BitMatrix bitMatrix) {
//
//    BufferedImage qrImage =
//        new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
//    Graphics2D graphics = qrImage.createGraphics();
//    graphics.setColor(Color.WHITE);
//    graphics.fillRect(0, 0, bitMatrix.getWidth(), bitMatrix.getHeight());
//    graphics.setColor(qrCodeColor);
//
//    for (int i = 0; i < bitMatrix.getHeight(); i++) {
//      for (int j = 0; j < bitMatrix.getWidth(); j++) {
//        if (bitMatrix.get(i, j)) {
//          graphics.fillRect(i, j, 1, 1);
//        }
//      }
//    }
//    return qrImage;
//  }
//
//  /**
//   * Adds the logo on QR image
//   *
//   * @param qrImage The QR image
//   * @param logo The logo image
//   * @return The combined image
//   */
//  private BufferedImage addLogo(BufferedImage qrImage, BufferedImage logo) {
//
//    // Calculating the delta height and width
//    float deltaHeight = qrImage.getHeight() - logo.getHeight();
//    float deltaWidth = qrImage.getWidth() - logo.getWidth();
//
//    // Combining the images
//    BufferedImage combined = new BufferedImage(qrCodeSize, qrCodeSize, BufferedImage.TYPE_INT_ARGB);
//    Graphics2D g = (Graphics2D) combined.getGraphics();
//    g.drawImage(qrImage, 0, 0, null);
//    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
//    g.drawImage(logo, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
//
//    return combined;
//  }
//
//  /**
//   * Creates the QR code from the provided information.
//   *
//   * @param data The data to be encoded in QR
//   * @param filePath The QR code file path
//   * @param logoFilePath The center logo file path
//   */
//  public void create(String data, Path filePath, String fileType, Path logoFilePath) {
//
//    try {
//      BufferedImage qrImage = this.createAndGet(data, logoFilePath);
//      ImageIO.write(qrImage, fileType, filePath.toFile());
//    } catch (IOException exception) {
////      log.error(exception.getMessage(), exception);
//      throw new InternalServiceException(exception.getMessage());
//    }
//  }
//
//  public BufferedImage createAndGet(String data, Path logoFilePath) {
//    try {
//      Map<EncodeHintType, Object> hintMap = this.getHintMap();
//      BitMatrix bitMatrix = this.getQRBitMatrix(data, hintMap);
//      BufferedImage qrImage = this.generateQRImage(bitMatrix);
//      if (logoFilePath != null) {
//        BufferedImage logo =
//            resize(
//                ImageIO.read(
//                    this.getClass().getClassLoader().getResourceAsStream(logoFilePath.toString())),
//                (int) (qrImage.getWidth() / 2.5),
//                (int) (qrImage.getHeight() / 2.5));
//        return this.addLogo(qrImage, logo);
//      }
//      return qrImage;
//    } catch (WriterException | IOException exception) {
////      log.error(exception.getMessage(), exception);
//      throw new InternalServiceException(exception.getMessage());
//    }
//  }
//}
