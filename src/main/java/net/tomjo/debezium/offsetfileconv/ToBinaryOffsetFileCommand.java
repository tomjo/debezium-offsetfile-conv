package net.tomjo.debezium.offsetfileconv;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static net.tomjo.debezium.offsetfileconv.ConversionUtil.*;

@Command(name = "binary", description = "Convert human readable offset file to binary offset file format used by org.apache.kafka.connect.storage.FileOffsetBackingStore")
public class ToBinaryOffsetFileCommand implements Runnable {

    @CommandLine.Option(
            names = {"-s", "--separator"},
            description = "Key-Value separator used for the human readable plain text file"
    )
    String separator = KEY_VAL_SEPARATOR;

    @Parameters(index = "0", description = "The source file path")
    Path inPath;

    @Parameters(index = "1", description = "The destination file path")
    Path outPath;

    public ToBinaryOffsetFileCommand() {
    }

    @Override
    public void run() {
        try {
            Map<ByteBuffer, ByteBuffer> data = readPlainTextOffsetFile(Files.newInputStream(inPath), separator);
            ByteArrayOutputStream converted = serializeMapOffsetFile(data);
            Files.write(outPath, converted.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
