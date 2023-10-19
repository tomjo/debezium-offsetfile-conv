package net.tomjo.debezium.offsetfileconv;

import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainLauncher;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusMainTest
class DebeziumOffsetFileConvTest {

    private static final String PLAIN_OFFSET_FILE_PATH = "src/test/resources/offsets.txt";
    private static final String BINARY_OFFSET_FILE_PATH = "src/test/resources/offsets.dat";

    @Test
    public void testPlainTextOffsetsfileToBinary(QuarkusMainLauncher launcher, @TempDir Path tempDir) throws IOException {
        Path outputPath = tempDir.resolve("offsets.dat");

        LaunchResult result = launcher.launch("binary", PLAIN_OFFSET_FILE_PATH, outputPath.toString());

        assertThat(outputPath).binaryContent().isEqualTo(Files.readAllBytes(Path.of(BINARY_OFFSET_FILE_PATH)));
        assertThat(result.exitCode()).isEqualTo(0);
    }

    @Test
    public void testBinaryOffsetsFileToPlainText(QuarkusMainLauncher launcher, @TempDir Path tempDir) throws IOException {
        Path outputPath = tempDir.resolve("offsets.txt");

        LaunchResult result = launcher.launch("plain", BINARY_OFFSET_FILE_PATH, outputPath.toString());

        assertThat(outputPath).content(UTF_8).isEqualTo(Files.readString(Path.of(PLAIN_OFFSET_FILE_PATH), UTF_8));
        assertThat(result.exitCode()).isEqualTo(0);
    }

    @Test
    public void testBinaryOffsetsFileToPlainTextToBinaryAgain(QuarkusMainLauncher launcher, @TempDir Path tempDir) throws IOException {
        Path outputPath = tempDir.resolve("offsets.txt");
        Path outputPath2 = tempDir.resolve("offsets.dat");

        LaunchResult result = launcher.launch("plain", BINARY_OFFSET_FILE_PATH, outputPath.toString());
        LaunchResult result2 = launcher.launch("binary", outputPath.toString(), outputPath2.toString());

        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(result2.exitCode()).isEqualTo(0);
        assertThat(outputPath2).binaryContent().isEqualTo(Files.readAllBytes(Path.of(BINARY_OFFSET_FILE_PATH)));
    }

    @Test
    public void testPlainTextOffsetsfileToBinaryToPlainTextAgain(QuarkusMainLauncher launcher, @TempDir Path tempDir) throws IOException {
        Path outputPath = tempDir.resolve("offsets.dat");
        Path outputPath2 = tempDir.resolve("offsets.txt");

        LaunchResult result = launcher.launch("binary", PLAIN_OFFSET_FILE_PATH, outputPath.toString());
        LaunchResult result2 = launcher.launch("plain", outputPath.toString(), outputPath2.toString());

        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(result2.exitCode()).isEqualTo(0);
        assertThat(outputPath2).content(UTF_8).isEqualTo(Files.readString(Path.of(PLAIN_OFFSET_FILE_PATH), UTF_8));
    }
}
