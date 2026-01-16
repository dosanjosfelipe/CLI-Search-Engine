package me.search.indexing;

import com.fasterxml.jackson.core.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.zip.ZipFile;

class FileVerification {

    private static final byte[] PDF_MAGIC = "%PDF-".getBytes(StandardCharsets.US_ASCII);
    private static final int TEXT_SAMPLE = 4096;
    private static final int HTML_SAMPLE = 1024;

    /* ===================== PDF ===================== */

    static boolean isPdfFile(File file) {
        try (InputStream in = new FileInputStream(file)) {

            byte[] buf = new byte[PDF_MAGIC.length];
            return in.read(buf) == buf.length &&
                    java.util.Arrays.equals(buf, PDF_MAGIC);

        } catch (IOException e) {
            return false;
        }
    }

    /* ===================== TEXT ===================== */

    static boolean isTextFile(File file) {
        try (InputStream in = new FileInputStream(file)) {

            byte[] buf = in.readNBytes(TEXT_SAMPLE);

            for (byte b : buf) {
                if (b == 0x00) return false;
            }

            CharsetDecoder decoder = StandardCharsets.UTF_8
                    .newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);

            decoder.decode(ByteBuffer.wrap(buf));
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    /* ===================== HTML ===================== */

    static boolean isHtmlFile(File file) {
        try (InputStream in = new FileInputStream(file)) {

            byte[] buf = in.readNBytes(HTML_SAMPLE);
            String s = new String(buf, StandardCharsets.UTF_8).toLowerCase();

            return s.contains("<html") || s.contains("<!doctype html");

        } catch (IOException e) {
            return false;
        }
    }

    /* ===================== JSON ===================== */

    static boolean isJsonFile(File file) {
        try (JsonParser p = new JsonFactory().createParser(file)) {

            return p.nextToken() != null;

        } catch (IOException e) {
            return false;
        }
    }

    /* ===================== DOCX ===================== */

    static boolean isDocxFile(File file) {
        try (ZipFile zip = new ZipFile(file)) {

            return zip.getEntry("[Content_Types].xml") != null &&
                    zip.getEntry("word/document.xml") != null;

        } catch (IOException e) {
            return false;
        }
    }
}
