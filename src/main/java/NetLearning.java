import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 015 15.12.16.
 */
public class NetLearning {

    private static final String CENTERED_IMAGES = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\centeredImg\\";
    private static final String NOT_CENTERED_IMAGES = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\notcenteredImg\\";

    private static final String TEST_CENTERED_IMAGES = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\testCenteredImg\\";
    private static final String TEST_NOT_CENTERED_IMAGES = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\testNotCenteredImg\\";

    private static boolean centered = false;

    private static final String NET_FILE = "not_centered.txt";

    public static void main(String[] args) throws IOException {
        File directory = new File(centered ? CENTERED_IMAGES : NOT_CENTERED_IMAGES);

        File[] directoryFiles = directory.listFiles();
        List<File> listFiles = Arrays.asList(directoryFiles);

        int initialSize = listFiles.size();

        //listFiles = listFiles.subList(1000, 1500);

        Collections.shuffle(listFiles);

        double[][] patterns = getPatterns(listFiles);
        double[][] answers = getAnswers(listFiles);

        int hiddenNumber = (int) Math.round(Math.sqrt(patterns[0].length * answers[0].length));

        Net net = new Net(patterns[0].length, 30, answers[0].length, patterns, answers);

        net.learn();
        net.saveWeights(NET_FILE);

        net.loadWeights(NET_FILE);

        File testDirectory = new File(centered ? TEST_CENTERED_IMAGES : TEST_NOT_CENTERED_IMAGES);

        File[] files = testDirectory.listFiles();

        List<File> test = new ArrayList<>();
        for (File file : files) {
            if (containsFileWithName(file.getName(), listFiles)) {
                test.add(file);
            }
        }

        System.out.println("_________________________________");

        int rightAnswers = 0, falseAnswers = 0;

        for (File imageFile: test) {
            String fileName = imageFile.getName();

            double[] rightAnswer = LettersCode.getLetterCode(Character.toString(fileName.charAt(0)));
            double[] enters = getImageArray(ImageIO.read(imageFile));

            net.setEnters(enters);
            double[] result = net.getResult();

            boolean arrayEqual = LettersCode.isArrayEqual(rightAnswer, result);
            if (arrayEqual) {
                rightAnswers++;

            } else {
                falseAnswers++;

                System.out.print("File: " + fileName + ". ");
                System.out.print("Right answer: " + LettersCode.getLetterByCode(rightAnswer) + " ");
                System.out.println("Result: " + LettersCode.getLetterByCode(result));
            }
        }

        System.out.println("Right: " + rightAnswers);
        System.out.println("False: " + falseAnswers);
    }

    private static double[][] getPatterns(List<File> letters) throws IOException {

        BufferedImage image = ImageIO.read(letters.get(0));

        int width = image.getWidth();
        int height = image.getHeight();

        int size = width * height;

        double[][] patterns = new double[letters.size()][size];

        for (int i = 0, length = letters.size(); i < length; i++) {
            patterns[i] = getImageArray(ImageIO.read(letters.get(i)));
        }

        return patterns;
    }

    private static double[] getImageArray (BufferedImage image) {
        int[] pixel;
        int cnt = 0;
        double[] imageArr = new double[image.getWidth() * image.getHeight()];

        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                pixel = image.getRaster().getPixel(y, x, new int[3]);
                imageArr[cnt++] = pixel[0] == 255 ? 0 : 1;
            }
        }
        return imageArr;
    }

    private static boolean containsFileWithName(String fileName, List<File> list) {
        String firstFileLetter = Character.toString(fileName.charAt(0));

        for (File file : list) {
            String listFileNameLetter = Character.toString(file.getName().charAt(0));
            if (firstFileLetter.equals(listFileNameLetter)) {
                return true;
            }
        }
        return false;
    }

    private static double[][] getAnswers(List<File> letters) {
        double[][] answers = new double[letters.size()][8];

        for (int i = 0, length = letters.size(); i < length; i++) {
            answers[i] = LettersCode.getLetterCode(Character.toString(letters.get(i).getName().charAt(0)));
        }

        return answers;
    }
}
