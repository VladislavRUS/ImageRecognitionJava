import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by User on 011 11.12.16.
 */
public class FileTransfer {
    public static void main(String[] args) throws IOException {
        String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\EnglishHnd\\English\\Hnd\\Img\\";
        String prefix  = "Sample0";

        String pathTo = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\testSet\\";

        for (int i = 11; i < 37; i++) {
            String directoryFrom = pathFrom + prefix + Integer.toString(i);
            System.out.println(directoryFrom);
            File file = new File(directoryFrom);
            File[] pictures = file.listFiles();

            for (int j = 0; j < 5; j++) {
                File letter = pictures[j];

                File targetDirectory = new File(pathTo + prefix + Integer.toString(i));
                targetDirectory.mkdir();

                File target = new File(pathTo + prefix + Integer.toString(i) + "\\" + letter.getName());

                Files.copy(letter.toPath(), target.toPath());
            }
        }
    }
}
