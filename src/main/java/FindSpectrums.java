import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by User on 014 14.12.16.
 */
public class FindSpectrums {

    private static final String CENTERED_SOURCE = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\centeredImg\\";
    private static final String NOT_CENTERED_SOURCE = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\notcenteredImg\\";

    private static final String CENTERED_SPECTRUM_TARGET = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\spectrumsCentered\\";
    private static final String NOT_CENTERED_SPECTRUM_TARGET = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\spectrumsNotCentered\\";

    private static final boolean centered = true;

    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File(centered ? CENTERED_SOURCE : NOT_CENTERED_SOURCE);

        File[] files = sourceDirectory.listFiles();

        for (int i = files.length - 1; i < files.length; i+= 200) {
            int start = i;
            int end = files.length - i > 200 ? start + 200 : files.length;

            Thread thread = new Thread(() -> {

                System.out.println(Thread.currentThread().getName() + " started from: " + start + " to " + end);

                try {
                    processFiles(getFilesRange(files, start, end));
                    System.out.println(Thread.currentThread().getName() + " finished!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        }
    }

    private static File[] getFilesRange(File[] files, int from, int to) {
        File[] range = new File[to - from];

        for (int i = from, j = 0; i < to; i++, j++) {
            range[j] = files[i];
        }

        return range;
    }

    private static void saveSpectrum(String fileName, double[] imageSpectrum) throws FileNotFoundException {
        File target = new File(fileName);
        PrintWriter writer = new PrintWriter(target);

        for (double v : imageSpectrum) {
            writer.println(v);
        }
        writer.flush();
        writer.close();
    }

    private static void processFiles(File[] files) throws IOException {
        for (File file : files) {
            double[] imageSpectrum = SpectrumProcessor.getImageSpectrum(ImageIO.read(file));

            saveSpectrum(centered
                            ? CENTERED_SPECTRUM_TARGET + file.getName().replace(".png", ".txt")
                            : NOT_CENTERED_SPECTRUM_TARGET + file.getName().replace(".png", ".txt"),
                    imageSpectrum);
            System.out.println("Processed file: " + file.getName());

            Thread.yield();
        }
    }
}
