import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by User on 011 11.12.16.
 */
public class FileName {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) throws IOException {
        //String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\";
        String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\test\\";
        String prefix = "Sample0";

        for (int i = 11; i < 37; i++) {
            String directoryPath = pathFrom + prefix + Integer.toString(i);

            File sampleDirectory = new File(directoryPath);

            File[] letters = sampleDirectory.listFiles();

            for (int j = 0; j < letters.length; j++) {
                File letter = letters[j];

                File letterWithNormalName = new File(pathFrom + prefix + Integer.toString(i) + "\\" + Character.toString(LETTERS.charAt(i-11)) + "_" + j + ".png");
                Files.copy(letter.toPath(), letterWithNormalName.toPath());

                Files.delete(letter.toPath());
            }
        }
    }
}
