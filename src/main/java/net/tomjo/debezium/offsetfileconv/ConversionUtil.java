package net.tomjo.debezium.offsetfileconv;

import org.apache.kafka.connect.util.SafeObjectInputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ConversionUtil {

    public static final String KEY_VAL_SEPARATOR = "||@@||";

    public static Map<ByteBuffer, ByteBuffer> readBinaryOffsetFile(InputStream offsetFileStream) throws IOException, ClassNotFoundException {
        Map<ByteBuffer, ByteBuffer> originalData = new HashMap<>();
        try (SafeObjectInputStream is = new SafeObjectInputStream(offsetFileStream)) {
            Object obj = is.readObject();
            if (!(obj instanceof HashMap<?,?>))
                throw new IOException("Expected HashMap but found " + obj.getClass());
            @SuppressWarnings("unchecked")
            Map<byte[], byte[]> raw =  (Map<byte[], byte[]>) obj;
            for (Map.Entry<byte[], byte[]> mapEntry : raw.entrySet()) {
                ByteBuffer key = (mapEntry.getKey() != null) ? ByteBuffer.wrap(mapEntry.getKey()) : null;
                ByteBuffer value = (mapEntry.getValue() != null) ? ByteBuffer.wrap(mapEntry.getValue()) : null;
                originalData.put(key, value);
            }
        }
        return originalData;
    }

    public static Map<ByteBuffer, ByteBuffer> readPlainTextOffsetFile(InputStream inputStream, String keyValSeparator) throws Exception {
        Map<ByteBuffer, ByteBuffer> data = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            bufferedReader.lines()
                    .map(line -> line.split(Pattern.quote(keyValSeparator)))
                    .forEach(parts -> data.put(utf8ToByteBuffer(parts[0]), utf8ToByteBuffer(parts[1])));
        }
        return data;
    }

    public static ByteArrayOutputStream serializeMapHumanReadable(Map<ByteBuffer, ByteBuffer> originalData, String keyValSeparator) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter pw = new PrintWriter(baos, false, StandardCharsets.UTF_8)) {
            for (Map.Entry<ByteBuffer, ByteBuffer> entry : originalData.entrySet()) {
                String key = byteBufferToUTF8(entry.getKey());
                String value = byteBufferToUTF8(entry.getValue());
                pw.write(key + keyValSeparator + value + "\n");
            }
            pw.flush();
        }
        return baos;
    }

    public static ByteArrayOutputStream serializeMapOffsetFile(Map<ByteBuffer, ByteBuffer> data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream os = new ObjectOutputStream(baos)) {
            Map<byte[], byte[]> raw = new HashMap<>();
            for (Map.Entry<ByteBuffer, ByteBuffer> mapEntry : data.entrySet()) {
                byte[] key = (mapEntry.getKey() != null) ? mapEntry.getKey().array() : null;
                byte[] value = (mapEntry.getValue() != null) ? mapEntry.getValue().array() : null;
                raw.put(key, value);
            }
            os.writeObject(raw);
        }
        return baos;
    }

    public static ByteBuffer utf8ToByteBuffer(String fixedValue) {
        return ByteBuffer.wrap(fixedValue.getBytes(StandardCharsets.UTF_8));
    }

    public static String byteBufferToUTF8(ByteBuffer key) {
        return new String(key.array(), StandardCharsets.UTF_8);
    }
}
