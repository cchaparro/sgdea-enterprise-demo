package demo.ports.out;

import java.io.InputStream;

public interface StoragePort {
    String save(InputStream file, String filename);
}