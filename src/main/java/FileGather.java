import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by User on 012 12.12.16.
 */
public class FileGather {
    public static void main(String[] args) throws IOException {
        //String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\";
        String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\test\\";
        String prefix = "Sample0";

        String destination = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\test\\all\\";

        for (int i = 11; i < 37; i++) {
            String directoryPath = pathFrom + prefix + Integer.toString(i);
            File directory = new File(directoryPath);

            File[] letters = directory.listFiles();

            for (File letter : letters) {
                Files.copy(letter.toPath(), new File(destination + letter.getName()).toPath());
            }
        }
    }
}
