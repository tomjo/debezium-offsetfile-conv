package net.tomjo.debezium.offsetfileconv;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

@QuarkusMain
public class DebeziumOffsetFileConv implements QuarkusApplication {

    @Inject
    CommandLine.IFactory factory;

    public static void main(String... args) {
        Quarkus.run(DebeziumOffsetFileConv.class, args);
    }

    @Override
    public int run(String... args) {
        return new CommandLine(new EntryCommand(), factory).execute(args);
    }

    @TopCommand
    @CommandLine.Command(name = "debezium-offsetfile-conv", mixinStandardHelpOptions = true, subcommands = {ToPlainTextOffsetFileCommand.class, ToBinaryOffsetFileCommand.class})
    static class EntryCommand {
    }

}
