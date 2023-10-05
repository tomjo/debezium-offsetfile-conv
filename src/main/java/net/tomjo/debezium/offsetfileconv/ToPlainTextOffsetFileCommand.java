package net.tomjo.debezium.offsetfileconv;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static net.tomjo.debezium.offsetfileconv.ConversionUtil.*;

@Command(name = "plain", description = "Convert binary offset file used by org.apache.kafka.connect.storage.FileOffsetBackingStore to a human readable plain text version")
public class ToPlainTextOffsetFileCommand implements Runnable {

    @CommandLine.Option(
            names = {"-s", "--separator"},
            description = "Key-Value separator used for the human readable plain text file"
    )
    String separator = KEY_VAL_SEPARATOR;

    @Parameters(index = "0", description = "The source file path")
    Path inPath;

    @Parameters(index = "1", description = "The destination file path")
    Path outPath;


    public ToPlainTextOffsetFileCommand() {
    }

    @Override
    public void run() {
        try {
            Map<ByteBuffer, ByteBuffer> originalData = readBinaryOffsetFile(Files.newInputStream(inPath.toFile().toPath()));
            ByteArrayOutputStream converted = serializeMapHumanReadable(originalData, separator);
            Files.write(outPath, converted.toByteArray());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
