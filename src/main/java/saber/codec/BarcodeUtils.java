package saber.codec;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public abstract class BarcodeUtils {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    private static final Map<EncodeHintType, Object> DEFAULT_ENCODE_HINTS = new HashMap<>();
    private static final Map<DecodeHintType, Object> DEFAULT_DECODE_HINTS = new HashMap<>();
    private static final int DEFAULT_TRUE_COLOR = 0xFF000000;
    private static final int DEFAULT_FALSE_COLOR = 0xFFFFFFFF;

    static {
        DEFAULT_ENCODE_HINTS.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET_NAME);
        DEFAULT_ENCODE_HINTS.put(EncodeHintType.MARGIN, 1);
        DEFAULT_ENCODE_HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    }

    public static BufferedImage encodeToImage(String contents, BarcodeFormat format, int width, int height, int imageType) throws WriterException {
        BitMatrix bitMatrix = encode(contents, format, width, height, DEFAULT_ENCODE_HINTS);
        return toBufferedImage(bitMatrix, imageType, DEFAULT_TRUE_COLOR, DEFAULT_FALSE_COLOR);
    }

    public static BufferedImage encodeToImage(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints, int imageType) throws WriterException {
        BitMatrix bitMatrix = encode(contents, format, width, height, hints);
        return toBufferedImage(bitMatrix, imageType, DEFAULT_TRUE_COLOR, DEFAULT_FALSE_COLOR);
    }

    public static String decodeToText(BufferedImage image) throws NotFoundException {
        return decode(image).getText();
    }

    public static String decodeToText(BufferedImage image, Map<DecodeHintType, ?> hints) throws NotFoundException {
        return decode(image, hints).getText();
    }

    public static BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return encode(contents, format, width, height, DEFAULT_ENCODE_HINTS);
    }

    public static BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        return writer.encode(contents, format, width, height, hints);
    }

    public static Result decode(BufferedImage image) throws NotFoundException {
        return decode(image, DEFAULT_DECODE_HINTS);
    }

    public static Result decode(BufferedImage image, Map<DecodeHintType, ?> hints) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        MultiFormatReader reader = new MultiFormatReader();
        return reader.decode(binaryBitmap, hints);
    }

    public static BufferedImage toBufferedImage(BitMatrix bitMatrix, int imageType) {
        return toBufferedImage(bitMatrix, imageType, DEFAULT_TRUE_COLOR, DEFAULT_FALSE_COLOR);
    }

    public static BufferedImage toBufferedImage(BitMatrix bitMatrix, int imageType, int trueColor, int falseColor) {
        int width = bitMatrix.getWidth(), height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, imageType);
        for (int x = 0; x < width; x++ ) {
            for (int y = 0; y < height; y++ ) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? trueColor : falseColor);
            }
        }
        return image;
    }

}
