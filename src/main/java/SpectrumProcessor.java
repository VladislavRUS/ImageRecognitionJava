import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by User on 012 12.12.16.
 */
public class SpectrumProcessor {

    private static class PixelValue {
        private double real;
        private double imaginary;

        double getReal() {
            return real;
        }

        void setReal(double real) {
            this.real = real;
        }

        double getImaginary() {
            return imaginary;
        }

        void setImaginary(double imaginary) {
            this.imaginary = imaginary;
        }
    }

    public static double[] getImageSpectrum(BufferedImage image) {
        double[][] signal = new double[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);

                int red = (rgb & 0x00ff0000) >> 16;

                if (red == 0) {
                    signal[i][j] = 0;

                } else {
                    signal[i][j] = 1;
                }
            }
        }

        PixelValue[][] spectrums = new PixelValue[image.getWidth()][image.getHeight()];

        for (int i = 0; i < signal.length; i++) {
            double[] s = signal[i];
            int N = s.length;

            for (int k = 0; k < N; k++) {
                double real = 0, imaginary = 0;

                for (int n = 0; n < N; n++) {
                    real += s[n] * Math.cos((-2 * Math.PI * k * n) / N);
                    imaginary += s[n] * Math.cos((-2 * Math.PI * k * n) / N);
                }

                PixelValue pixelValue = new PixelValue();
                pixelValue.setReal(real);
                pixelValue.setImaginary(imaginary);

                spectrums[i][k] = pixelValue;
            }
        }

        PixelValue[] complexSignal = new PixelValue[image.getWidth() * image.getHeight()];
        int cnt = 0;
        for (int i = 0; i < spectrums[0].length; i++) {
            for (int j = 0; j < spectrums.length; j++) {
                complexSignal[cnt++] = spectrums[j][i];
            }
        }

        double[] spectrum = new double[image.getWidth() * image.getHeight()];
        int N = complexSignal.length, cntSpectrum = 0;
        double maxValue = Double.MIN_VALUE, minValue = Integer.MAX_VALUE;

        for (int k = 0; k < N; k++) {
            double real = 0, imaginary = 0;

            for (int n = 0; n < N; n++) {
                real += complexSignal[n].getReal() * Math.cos((-2 * Math.PI * k * n) / N) - complexSignal[n].getImaginary() * Math.sin((-2 * Math.PI * k * n) / N);
                imaginary += complexSignal[n].getReal() * Math.sin((-2 * Math.PI * k * n) / N) + complexSignal[n].getImaginary() * Math.cos((-2 * Math.PI * k * n) / N);
            }

            double value = Math.sqrt(real * real + imaginary * imaginary);
            if (value > maxValue) {
                maxValue = value;
            }

            if (value < minValue) {
                minValue = value;
            }

            spectrum[cntSpectrum++] = value;
        }

        for (int i = 0; i < spectrum.length; i++) {
            spectrum[i] = scaleBetween(spectrum[i], 0, 1, minValue, maxValue);
        }

        return Arrays.copyOfRange(spectrum, 0, spectrum.length/2);
    }

    private static double scaleBetween(double unscaledNum, double minAllowed, double maxAllowed, double min, double max) {
        return (maxAllowed - minAllowed) * (unscaledNum - min) / (max - min) + minAllowed;
    }
}
