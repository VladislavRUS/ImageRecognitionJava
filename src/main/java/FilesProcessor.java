import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 014 14.12.16.
 */
public class FilesProcessor {

    private static final String SOURCE = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\EnglishHnd\\English\\Hnd\\Img\\";

    private static final String BUFFER_FOLDER = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\buffer\\";

    private static final String DESTINATION_CENTERED = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\centeredImg\\";
    private static final String DESTINATION_NOT_CENTERED = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\notcenteredImg\\";

    private static boolean centered = false;

    private static final String SYMBOLS = "0123456789" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws IOException {

        List<File> images = getImagesFromFolder(SOURCE);
        copyImagesToBufferFolder(images, BUFFER_FOLDER);

        System.out.println("Going to take files from buffer folder...");
        List<File> copiedImages = getImagesFromFolder(BUFFER_FOLDER);

        System.out.println("Processing images...");
        for (File copiedImage : copiedImages) {
            processImage(ImageIO.read(copiedImage), new File(centered ? DESTINATION_CENTERED + copiedImage.getName(): DESTINATION_NOT_CENTERED + copiedImage.getName()));
        }

        System.out.println("Deleting from folder...");
        deleteFromBuffer(copiedImages);
    }

    private static void deleteFromBuffer(List<File> copiedImages) throws IOException {
        for (File copiedImage : copiedImages) {
            Files.delete(copiedImage.toPath());
        }
    }

    private static void copyImagesToBufferFolder(List<File> images, String to) throws IOException {
        File destination = new File(to);

        if (!destination.exists()) {
            destination.mkdirs();
        }

        int cnt = 0;

        for (File image : images) {
            Files.copy(image.toPath(), new File(to + getNewName(image.getName(), cnt++)).toPath());
        }
    }

    private static String getNewName(String fileName, int cnt) {
        String[] split = fileName.split("-");

        String firstPart = split[0].replace("img", "");
        int letterIndex = Integer.parseInt(firstPart);

        String symbol = Character.toString(SYMBOLS.charAt(letterIndex - 1));

        return symbol + "-" + cnt + "-" + split[1];
    }

    private static List<File> getImagesFromFolder(String source) throws IOException {
        System.out.println("Source folder: " + source);

        File directory = new File(source);

        File[] sampleFolders = directory.listFiles();
        checkFiles(sampleFolders);

        System.out.println("Checked files in folder...");

        List<File> images = new ArrayList<>();
        for (File folder : sampleFolders) {
            images.addAll(getImageFiles(folder));
        }

        return images;
    }

    private static List<File> getImageFiles(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            checkFiles(files);
            return Arrays.asList(files);

        } else {
            return Arrays.asList(file);
        }
    }

    private static void checkFiles(File[] files) {
        if (files == null || files.length == 0) {
            throw new RuntimeException("Empty!");
        }
    }

    private static void processImage(BufferedImage letter, File file) throws IOException {
        BufferedImage letterCopy = scale(letter);

        if (centered) {
            int xSum = 0, ySum = 0, massSum = 0;
            for (int x = 0; x < letterCopy.getWidth(); x++) {
                for (int y = 0; y < letterCopy.getHeight(); y++) {
                    int rgb = letterCopy.getRGB(x, y);

                    int red = (rgb & 0x00ff0000) >> 16;
                    int green = (rgb & 0x0000ff00) >> 8;
                    int blue = (rgb & 0x000000ff);

                    if (red == 0 && green == 0 && blue == 0) {
                        xSum += x;
                        ySum += y;
                        massSum += 1;
                    }
                }
            }

            int xCenter = xSum / massSum;
            int yCenter = ySum / massSum;

            int actualX = letterCopy.getWidth() / 2;
            int actualY = letterCopy.getHeight() / 2;

            int xDiff = actualX - xCenter;
            int yDiff = actualY - yCenter;

            BufferedImage image = new BufferedImage(letterCopy.getWidth(), letterCopy.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < letterCopy.getWidth(); x++) {
                for (int y = 0; y < letterCopy.getHeight(); y++) {
                    int rgb = letterCopy.getRGB(x, y);

                    int red = (rgb & 0x00ff0000) >> 16;

                    int xPos = x + xDiff;
                    int yPos = y + yDiff;

                    if (xPos > letterCopy.getWidth() - 1) {
                        xPos = xPos - letterCopy.getWidth();
                    }

                    if (xPos < 0) {
                        xPos = letterCopy.getWidth() + xPos;
                    }

                    if (yPos > letterCopy.getHeight() - 1) {
                        yPos = yPos - letterCopy.getHeight();
                    }

                    if (yPos < 0) {
                        yPos = letterCopy.getHeight() + yPos;
                    }

                    if (red == 0) {
                        image.setRGB(xPos, yPos, 0);

                    } else {
                        image.setRGB(xPos, yPos, 0xffffffff);
                    }
                }
            }

            ImageIO.write(image, "png", file);

        } else {
            ImageIO.write(letterCopy, "png", file);
        }
    }

    private static BufferedImage scale(BufferedImage original) {
        int width = original.getWidth() / 22, height = original.getHeight() / 22;

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();

        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(original, 0, 0, width, height, null);
        graphics.dispose();

        return resizedImage;
    }
}
