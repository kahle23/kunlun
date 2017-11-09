package saber.codec;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kahle
 */
public class Barcode {
    private static final int DEFAULT_TRUE_COLOR = 0xFF000000;
    private static final int DEFAULT_FALSE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

    public static Barcode on() {
        return new Barcode();
    }

    public static Barcode on(BarcodeFormat barcodeFormat) {
        return new Barcode().setBarcodeFormat(barcodeFormat);
    }

    public static Barcode on(int width, int height) {
        return new Barcode().setWidth(width).setHeight(height);
    }

    public static Barcode on(BarcodeFormat barcodeFormat, int width, int height) {
        return new Barcode().setWidth(width)
                .setHeight(height).setBarcodeFormat(barcodeFormat);
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

    private String charset = Charset.defaultCharset().name();
    private Map<EncodeHintType, Object> encodeHints = new HashMap<>();
    private Map<DecodeHintType, Object> decodeHints = new HashMap<>();
    private int trueColor = DEFAULT_TRUE_COLOR;
    private int falseColor = DEFAULT_FALSE_COLOR;
    private int imageType = DEFAULT_IMAGE_TYPE;
    private BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;
    private int width = 500;
    private int height = 500;

    private Barcode() {
        encodeHints.put(EncodeHintType.CHARACTER_SET, charset);
        encodeHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        encodeHints.put(EncodeHintType.MARGIN, 1);
        decodeHints.put(DecodeHintType.CHARACTER_SET, charset);
    }

    public String getCharset() {
        return charset;
    }

    public Barcode setCharset(String charset) {
        this.charset = charset;
        encodeHints.put(EncodeHintType.CHARACTER_SET, charset);
        decodeHints.put(DecodeHintType.CHARACTER_SET, charset);
        return this;
    }

    public Map<EncodeHintType, Object> getEncodeHints() {
        return encodeHints;
    }

    public Barcode setEncodeHints(Map<EncodeHintType, Object> encodeHints) {
        this.encodeHints = encodeHints;
        return this;
    }

    public Barcode addEncodeHints(Map<EncodeHintType, Object> encodeHints) {
        this.encodeHints.putAll(encodeHints);
        return this;
    }

    public Barcode addEncodeHint(EncodeHintType encodeHintType, Object value) {
        this.encodeHints.put(encodeHintType, value);
        return this;
    }

    public Map<DecodeHintType, Object> getDecodeHints() {
        return decodeHints;
    }

    public Barcode setDecodeHints(Map<DecodeHintType, Object> decodeHints) {
        this.decodeHints = decodeHints;
        return this;
    }

    public Barcode addDecodeHints(Map<DecodeHintType, Object> decodeHints) {
        this.decodeHints.putAll(decodeHints);
        return this;
    }

    public Barcode addDecodeHint(DecodeHintType decodeHintType, Object value) {
        this.decodeHints.put(decodeHintType, value);
        return this;
    }

    public int getTrueColor() {
        return trueColor;
    }

    public Barcode setTrueColor(int trueColor) {
        this.trueColor = trueColor;
        return this;
    }

    public int getFalseColor() {
        return falseColor;
    }

    public Barcode setFalseColor(int falseColor) {
        this.falseColor = falseColor;
        return this;
    }

    public int getImageType() {
        return imageType;
    }

    public Barcode setImageType(int imageType) {
        this.imageType = imageType;
        return this;
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public Barcode setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Barcode setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Barcode setHeight(int height) {
        this.height = height;
        return this;
    }

    public BitMatrix encode(String content) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        return writer.encode(content, barcodeFormat, width, height, encodeHints);
    }

    public Result decode(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        MultiFormatReader reader = new MultiFormatReader();
        return reader.decode(binaryBitmap, decodeHints);
    }

    public BufferedImage encodeToImage(String content) throws WriterException {
        BitMatrix bitMatrix = this.encode(content);
        return toBufferedImage(bitMatrix, imageType, trueColor, falseColor);
    }

    public boolean encodeToImage(String content, File file) throws WriterException, IOException {
        BufferedImage image = this.encodeToImage(content);
        String extension = FilenameUtils.getExtension(file.toString());
        if (StringUtils.isBlank(extension)) {
            extension = "jpg";
            file = new File(file.toString() + ".jpg");
        }
        return ImageIO.write(image, extension, file);
    }

    public String decodeToText(BufferedImage image) throws NotFoundException {
        return this.decode(image).getText();
    }

    public String decodeToText(File file) throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(file);
        return this.decodeToText(image);
    }

}
