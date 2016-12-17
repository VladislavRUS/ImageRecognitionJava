import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by User on 011 11.12.16.
 */
public class ImageScale {
    public static void main(String[] args) throws IOException {
        //String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\EnglishHnd\\English\\Hnd\\Img\\";
        String pathFrom = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\testSet\\";
        String prefix = "Sample0";

        //String pathTo = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\dataSet\\";
        String pathTo = "C:\\Users\\User\\IdeaProjects\\ImageProcessing\\test\\";

        for (int i = 11; i < 37; i++) {
            String directoryFrom = pathFrom + prefix + Integer.toString(i);

            File file = new File(directoryFrom);
            File[] pictures = file.listFiles();

            assert pictures != null;

            File targetDirectory = new File(pathTo + prefix + Integer.toString(i));
            targetDirectory.mkdir();

            for (int j = 0; j < pictures.length; j++) {
                File letter = pictures[j];
                BufferedImage image = ImageIO.read(letter);
                processImage(image, new File(pathTo + prefix + Integer.toString(i) + "\\" + letter.getName()));
            }
        }
    }

    public static void processImage(BufferedImage letter, File file) throws IOException {
        BufferedImage letterCopy = scale(letter);

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
