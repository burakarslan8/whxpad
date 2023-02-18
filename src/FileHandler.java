import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHandler {
    public static String openFile(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded);
    }
}