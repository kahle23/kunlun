package saber.util;

import saber.codec.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public enum FileType {
    JPEG(            "JPEG",                   "jpg",          Pattern.compile("(?i)FFD8FFE000104A464946.*")),
    PNG(             "PNG",                    "png",          Pattern.compile("(?i)89504E470D0A1A0A0000.*")),
    GIF(             "GIF",                    "gif",          Pattern.compile("(?i)47494638396126026F01.*")),
    TIFF(            "TIFF",                   "tif",          Pattern.compile("(?i)49492A00227105008037.*")),
    BMP16(           "Windows 16 Bitmap",      "bmp",          Pattern.compile("(?i)424D228C010000000000.*")),
    BMP24(           "Windows 24 Bitmap",      "bmp",          Pattern.compile("(?i)424D8240090000000000.*")),
    BMP256(          "Windows 256 Bitmap",     "bmp",          Pattern.compile("(?i)424D8E1B030000000000.*")),
    PSD(             "Adobe Photoshop",        "psd",          Pattern.compile("(?i)38425053000100000000.*")),
    DWG(             "CAD",                    "dwg",          Pattern.compile("(?i)41433130313500000000.*")),
    RTF(             "Rich Text Format",       "rtf",          Pattern.compile("(?i)7B5C727466317D|(?i)7B5C72746631205C616E7369205C616E7369637067393336205C646566663020.*")),
    DOC(             "MS Word 2003",           "doc",          Pattern.compile("(?i)D0CF11E0A1B11AE1000000000000000000000000000000003E000300FEFF0900.*")),//final
    XLS(             "MS Excel 2003",          "xls",          Pattern.compile("(?i)D0CF11E0A1B11AE1000000000000000000000000000000003E000300FEFF0900.*")),//final
    PPT(             "MS PowerPoint 2003",     "ppt",          Pattern.compile("(?i)D0CF11E0A1B11AE1000000000000000000000000000000003E000300FEFF0900.*")),//final
    WPS(             "Kingsoft Word",          "wps",          Pattern.compile("(?i)D0CF11E0A1B11AE10000.*")),
    ET(              "Kingsoft Excel",         "et",           Pattern.compile("(?i)D0CF11E0A1B11AE10000.*")),
    DPS(             "Kingsoft PowerPoint",    "dps",          Pattern.compile("(?i)D0CF11E0A1B11AE10000.*")),
    VSD(             "Visio",                  "vsd",          Pattern.compile("(?i)D0CF11E0A1B11AE10000.*")),
    MSI(             "Windows Installer",      "msi",          Pattern.compile("(?i)D0CF11E0A1B11AE10000.*")),
    MDB(             "MS Access",              "mdb",          Pattern.compile("(?i)5374616E64617264204A.*")),
    DOCX(            "MS Word 2007",           "docx",         Pattern.compile("(?i)504B03040A0000000000874EE24000000000000000000000000009000000646F.*")),//maybefinal
    XLSX(            "MS Excel 2007",          "xlsx",         Pattern.compile("(?i)504B030414000600080000002100CA84BF819B0100003303000010000801646F.*|(?i)504B03040A0000000000874EE24000000000000000000000000009000000646F.*")),//final
    PPTX(            "MS PowerPoint 2007",     "pptx",         Pattern.compile("(?i)504B030414000600080000002100A28760E1A6010000C40B0000130008025B43.*|(?i)504B03040A0000000000874EE240000000000000000000000000040000007070.*")),//final
    PS(              "Postscript",             "ps",           Pattern.compile("(?i)252150532D41646F6265.*")),
    PDF(             "Adobe Acrobat",          "pdf",          Pattern.compile("(?i)255044462D312E350D0A.*")),
    TORRENT(         "Bit Torrent",            "torrent",      Pattern.compile("(?i)6431303A637265617465.*")),
    EPS(             "Postscript",             "eps",          Pattern.compile("(?i)252150532D41646F6265.*")),
    ZIP(             "ZIP Archive",            "zip",          Pattern.compile("(?i)504B0304140000000800.*|(?i)504B0506000000000000.*")),
    RAR(             "RAR Archive",            "rar",          Pattern.compile("(?i)526172211A0700CF907300000D00000000000000.*")),
    CLASS(           "CLASS",                  "class",        Pattern.compile("(?i)CAFEBABE.{8}.*")),
    JAR(             "JAR",                    "jar",          Pattern.compile("(?i)504B03040A0000000[0-9A-Fa-f]{16}0000000000000000000090000004D45.*")),//final
    EXE(             "EXE",                    "exe",          Pattern.compile("(?i)4D5A90000300000004000000FFFF0000B8000000000000004000000000000000.*")),//final
    CHM(             "CHM",                    "chm",          Pattern.compile("(?i)49545346030000006000.*")),
    GZ(              "GZ",                     "gz",           Pattern.compile("(?i)1F8B0800000000000000.*")),
    MXP(             "MXP",                    "mxp",          Pattern.compile("(?i)04000000010000001300.*")),
    WAV(             "Wave",                   "wav",          Pattern.compile("(?i)52494646E27807005741.*")),
    AVI(             "AVI",                    "avi",          Pattern.compile("(?i)52494646D07D60074156.*")),
    MPG(             "MPEG",                   "mpg",          Pattern.compile("(?i)000001BA210001000180.*")),
    ASF(             "Windows Media",          "asf",          Pattern.compile("(?i)3026B2758E66CF11A6D9.*")),
    WMV(             "Windows Media",          "wmv",          Pattern.compile("(?i)3026B2758E66CF11A6D9.*")),
    MID(             "MIDI",                   "mid",          Pattern.compile("(?i)4D546864000000060001.*")),
    MP4(             "MP4",                    "mp4",          Pattern.compile("(?i)00000020667479706D70|(?i)000000206674797069736F6D0000020069736F6D69736F32617663316D703431.*")),
    MP3(             "MP3",                    "mp3",          Pattern.compile("(?i)49443303000000002176.*")),
    RMVB(            "Real Media",             "rmvb",         Pattern.compile("(?i)2E524D46000000120001.*")),
    RM(              "Real Media",             "rm",           Pattern.compile("(?i)2E524D46000000120001.*")),
    FLV(             "FLV",                    "flv",          Pattern.compile("(?i)464C5601050000000900.*")),
    F4V(             "f4v",                    "f4v",          Pattern.compile("(?i)464C5601050000000900.*")),
    DBX(             "Outlook Express",        "dbx",          Pattern.compile("(?i)CFAD12FEC5FD746F.*")),
    PST(             "Outlook",                "pst",          Pattern.compile("(?i)2142444E.*")),
    WPD(             "WordPerfect",            "wpd",          Pattern.compile("(?i)FF575043.*")),
    QDF(             "Quicken",                "qdf",          Pattern.compile("(?i)AC9EBD8F.*")),
    PWL(             "Windows Password",       "pwl",          Pattern.compile("(?i)E3828596.*")),
    RAM(             "Real Audio",             "ram",          Pattern.compile("(?i)2E7261FD.*")),
    MOV(             "Quicktime",              "mov",          Pattern.compile("(?i)6D6F6F76.*")),

    UNKNOWN(         "Unknown",              "unknown",        Pattern.compile(".*"));

    private String typeName;
    private String extension;
    private Pattern fileHeader;

    FileType(String typeName, String extension, Pattern fileHeader) {
        this.typeName = typeName;
        this.extension = extension;
        this.fileHeader = fileHeader;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getExtension() {
        return extension;
    }

    public Pattern getFileHeader() {
        return fileHeader;
    }

    @Override
    public String toString() {
        return typeName + " (" + extension + ")";
    }

    private static Hex hex = Hex.on(true);
    private static final int DEFAULT_HEADER_LENGTH = 1024;

    public static List<FileType> check(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] buf = new byte[DEFAULT_HEADER_LENGTH];
        int read = in.read(buf);
        in.close();
        return check(Arrays.copyOfRange(buf, 0, read));
    }

    public static List<FileType> check(byte[] data) {
        String hexString = hex.encodeToString(data);
        hexString = hexString.length() > DEFAULT_HEADER_LENGTH
                ? hexString.substring(0, DEFAULT_HEADER_LENGTH) : hexString;
        return check(hexString);
    }

    public static List<FileType> check(String hexString) {
        List<FileType> fileTypes = new ArrayList<>();
        for (FileType fileType : FileType.values()) {
            if (fileType.fileHeader.matcher(hexString).matches()) {
                fileTypes.add(fileType);
            }
        }
        if (fileTypes.size() > 1) fileTypes.remove(fileTypes.size() - 1);
        return fileTypes;
    }

    public static String fileHeader(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] buf = new byte[DEFAULT_HEADER_LENGTH];
        int read = in.read(buf);
        in.close();
        String result = hex.encodeToString(Arrays.copyOfRange(buf, 0, read));
        result = result.length() > DEFAULT_HEADER_LENGTH
                ? result.substring(0, DEFAULT_HEADER_LENGTH) : result;
        return result;
    }

}
