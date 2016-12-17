import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * Created by User on 012 12.12.16.
 */
public class Main {

    private static final String dataSetPath = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\all\\";
    private static final String dataSetPathSpectrums = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\spectrums\\";
    private static final String testSetPath = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\test\\all";

    private static int WIDTH = 54;
    private static int HEIGHT = 40;

    public static boolean TRAIN = false;

    public static void main(String[] args) throws IOException {
        File dataSet = new File(dataSetPath);

        File[] files = dataSet.listFiles();

        long s = System.currentTimeMillis();


        for (int i = 0; i < files.length; i += 55) {
            File[] letters = getPart(files, i, i + 55);

            Thread thread = new Thread(() -> {
                int cnt = 0;
                for (File file : letters) {
                    double[] imageSpectrum = new double[0];
                    try {
                        imageSpectrum = SpectrumProcessor.getImageSpectrum(ImageIO.read(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        saveSpectrum(imageSpectrum, dataSetPathSpectrums + file.getName().replace(".png", ".txt"));
                        Thread.yield();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread: " + Thread.currentThread().getName() + ", done:" + cnt++);
                }
            });

            thread.start();
        }

/*
        System.out.println((System.currentTimeMillis() - s)/60);
        if (true) return;

        double[][] patterns = getPatterns(files);
        double[][] answers = getAnswers(files);

        System.out.println("Start training...");

        Net net = new Net(WIDTH * HEIGHT, 32, answers[0].length, patterns, answers);

        if (TRAIN) {
            long start = System.currentTimeMillis();
            net.learn();
            System.out.println((System.currentTimeMillis() - start) / 1000);

            net.saveWeights();

        } else {
            net.loadWeights();
        }

        File testSet = new File(testSetPath);
        File[] testSamples = testSet.listFiles();

        int success = 0, failed = 0;

        for (File testSample : testSamples) {
            net.setEnters(getImageArray(ImageIO.read(testSample)));

            double[] result = net.getResult();
            double[] rightAnswer = LettersCode.getLetterCode(Character.toString(testSample.getName().charAt(0)));

            if (LettersCode.isArrayEqual(result, rightAnswer)) {
                success++;

            } else {
                failed++;
                System.out.println(testSample.getName());
            }
        }

        System.out.println("Success: " + success);
        System.out.println("Failed: " + failed);*/
    }

    private static double[][] getPatterns(File[] letters) throws IOException {

        BufferedImage image = ImageIO.read(letters[0]);

        int width = image.getWidth();
        int height = image.getHeight();

        int size = width * height;

        double[][] patterns = new double[letters.length][size];

        for (int i = 0; i < letters.length; i++) {
            patterns[i] = getImageArray(ImageIO.read(letters[i]));
        }

        return patterns;
    }

    private static double[] getImageArray (BufferedImage image) {
        int[] pixel;
        int cnt = 0;
        double[] imageArr = new double[image.getWidth() * image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixel = image.getRaster().getPixel(x, y, new int[3]);
                imageArr[cnt++] = pixel[0] == 255 ? 0 : 1;
            }
        }
        return imageArr;
    }

    private static double[][] getAnswers(File[] letters) {
        double[][] answers = new double[letters.length][4];

        for (int i = 0; i < letters.length; i++) {
            answers[i] = LettersCode.getLetterCode(Character.toString(letters[i].getName().charAt(0)));
        }
        return answers;
    }

    private static void saveSpectrum(double[] spectrum, String fileName) throws IOException {
        File imageSpectrum = new File(fileName);
        imageSpectrum.createNewFile();

        PrintWriter writer = new PrintWriter(new FileOutputStream(imageSpectrum));
        for (int i = 0; i < spectrum.length; i++) {
            writer.println(spectrum[i]);
        }

        writer.flush();
        writer.close();
    }

    public static File[] getPart(File[] full, int from, int to) {
        File[] files = new File[to - from];

        for (int i = from, j = 0; i < to; i++, j++) {
            files[j] = full[i];
        }

        return files;
    }
}
