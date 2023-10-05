# debezium-offsetfile-conv

Utility to convert to and from *org.apache.kafka.connect.storage.FileOffsetBackingStore* based offset file (java serialized *HashMap<ByteBuffer,ByteBuffer>*) to human-readable file

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
