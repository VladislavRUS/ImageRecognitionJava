import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by User on 014 14.12.16.
 */
public class TestFiles {
    private static final String CENTERED_SOURCE = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\centeredImg\\";
    private static final String NOT_CENTERED_SOURCE = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\notcenteredImg\\";

    private static final String TEST_CENTERED_TARGET = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\testCenteredImg\\";
    private static final String TEST_NOT_CENTERED_TARGET = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\testNotCenteredImg\\";

    private static boolean centered = false;

    private static int[] range = {51, 52, 53, 54, 55};

    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File(centered ? CENTERED_SOURCE : NOT_CENTERED_SOURCE);

        File[] files = sourceDirectory.listFiles();

        for (File file : files) {
            String fileName = file.getName();
            String[] split = fileName.split("-");

            int numberInClass = Integer.parseInt(split[2].replaceAll(".png", ""));
            if (inRange(numberInClass, range)) {
                Files.copy(file.toPath(), new File(centered ? TEST_CENTERED_TARGET + fileName : TEST_NOT_CENTERED_TARGET + fileName).toPath());
                Files.delete(file.toPath());
            }
        }
    }

    private static boolean inRange(int number, int[] range) {
        for (int i : range) {
            if (i == number) {
                return true;
            }
        }
        return false;
    }
}
