# debezium-offsetfile-conv

`debezium-offsetfile-conv` is a commandline utility to convert to and from **org.apache.kafka.connect.storage.FileOffsetBackingStore** based offset file `offsets.dat` (java serialized **HashMap<ByteBuffer,ByteBuffer>** created by Debezium) to human-readable text based file.

## Format

The text based file consists of json lines, one line per entry of the serialized HashMap. Key and value are separated by default with `||@@||`, but this is configurable.

Example:
```
["pulsar",{"server":"foo"}]||@@||{"transaction_id":null,"ts_sec":1695302112,"file":"mysql-bin.001097","pos":293351573,"gtids":"fa712d50-ae8e-11ec-bc29-57e50ee87685:1-879838146","server_id":3}
```

## Configuration

Following subcommands are available:
* `plain <offsets.dat> <plain.dat>`
  * converts offsets.dat to a text based file
  * `-s,--separator <separator>` to override the default key-value separator
* `binary <plain.dat> <offsets.dat>`
  * converts the text based file to the offsets.dat binary format
  * `-s,--separator <separator>` to override the default key-value separator

```shell
Usage: debezium-offsetfile-conv [-hV] [COMMAND]
    -h, --help      Show this help message and exit.
    -V, --version   Print version information and exit.
Commands:
    plain   Convert binary offset file used by org.apache.kafka.connect.storage.
            FileOffsetBackingStore to a human readable plain text version
    binary  Convert human readable offset file to binary offset file format used
            by org.apache.kafka.connect.storage.FileOffsetBackingStore
```

## Installation

### Pre-built binaries

You can download pre-built binaries from the latest GitHub release: https://github.com/tomjo/debezium-offsetfile-conv/releases/latest

### Container image

Images available at https://ghcr.io/tomjo/debezium-offsetfile-conv

### From source

Requires Java 17 (or later).

```shell
./gradlew build
```

It is built with the [quarkus](https://quarkus.io) framework, so specify environment variable `QUARKUS_PACKAGE_TYPE` to build the different variants (uber-jar, native).

The container build requires a container build tool that handles Dockerfile such as Docker, Podman, Kaniko, ...
