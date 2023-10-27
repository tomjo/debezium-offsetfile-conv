# debezium-offsetfile-conv

`debezium-offsetfile-conv` is a commandline utility to convert to and from **org.apache.kafka.connect.storage.FileOffsetBackingStore** based offset file `offsets.dat` (java serialized **HashMap<ByteBuffer,ByteBuffer>** created by Debezium) to human-readable text based file.

## Format

The text based file consists of json lines, one line per entry of the serialized HashMap. Key and value are separated by default with `||@@||`, but this is configurable.

## Configuration

Following subcommands are available:
* `plain <offsets.dat> <plain.dat>`
  * converts offsets.dat to a text based file
  * `-s,--separator <separator>` to override the default key-value separator
* `binary <plain.dat> <offsets.dat>`
  * converts the text based file to the offsets.dat binary format
  * `-s,--separator <separator>` to override the default key-value separator

```
Usage: debezium-offsetfile-conv [-hV] [COMMAND]
    -h, --help      Show this help message and exit.
    -V, --version   Print version information and exit.
Commands:
    plain   Convert binary offset file used by org.apache.kafka.connect.storage.
            FileOffsetBackingStore to a human readable plain text version
    binary  Convert human readable offset file to binary offset file format used
            by org.apache.kafka.connect.storage.FileOffsetBackingStore
```
