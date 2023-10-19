package net.tomjo.debezium.offsetfileconv;

import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainIntegrationTest;
import io.quarkus.test.junit.main.QuarkusMainLauncher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusMainIntegrationTest
class DebeziumOffsetFileConvIntegrationTest extends DebeziumOffsetFileConvTest {

    private static final String PLAIN_OFFSET_FILE_PATH = "src/test/resources/offsets.txt";
    private static final String BINARY_OFFSET_FILE_PATH = "src/test/resources/offsets.dat";


    @Test
    public void repro(QuarkusMainLauncher launcher, @TempDir Path tempDir) throws IOException {
        System.out.println("launcher="+launcher+" , tempDir="+tempDir);
        Path outputPath = tempDir.resolve("offsets.dat");

        LaunchResult result = launcher.launch("binary", PLAIN_OFFSET_FILE_PATH, outputPath.toString());

        System.out.println(result);
        System.out.println(result.getOutput());
        System.out.println(result.getErrorOutput());
        System.out.println(result.exitCode());
        assertThat(result.getOutput()).isEqualTo("");
        assertThat(outputPath).binaryContent().isEqualTo(Files.readAllBytes(Path.of(BINARY_OFFSET_FILE_PATH)));
        assertThat(result.exitCode()).isEqualTo(0);
    }
}
