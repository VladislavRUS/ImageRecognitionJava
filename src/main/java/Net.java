import java.io.*;

public class Net {
    private final double alpha = 0.1;

    private double[] enters;
    private double[] hidden;
    private double[] out;

    private double[][] wEH;
    private double[][] wHO;

    private double[][] patterns;
    private double[][] answers;


    Net(int entersNumber, int hiddenNumber, int outNumber, double[][] patterns, double[][] answers) {
        enters = new double[entersNumber];
        hidden = new double[hiddenNumber];
        out = new double[outNumber];
        wEH = new double[enters.length][hidden.length];
        wHO = new double[hidden.length][out.length];

        this.patterns = patterns;
        this.answers = answers;

        init();
    }

    public void saveWeights(String fileName) {
        File f = new File(fileName);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(f));
            for (int i = 0; i < enters.length; i++) {
                for (int j = 0; j < hidden.length; j++) {
                    writer.println(wEH[i][j]);
                }
            }
            writer.flush();
            for (int i = 0; i < hidden.length; i++) {
                for (int j = 0; j < out.length; j++) {
                    writer.println(wHO[i][j]);
                }
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadWeights(String fileName) {
        File f = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            for (int i = 0; i < enters.length; i++) {
                for (int j = 0; j < hidden.length; j++) {
                    String line = reader.readLine();
                    wEH[i][j] = Double.parseDouble(line);
                }
            }
            for (int i = 0; i < hidden.length; i++) {
                for (int j = 0; j < out.length; j++) {
                    String line = reader.readLine();
                    wHO[i][j] = Double.parseDouble(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        for (int i = 0; i < enters.length; i++) {
            for (int j = 0; j < hidden.length; j++) {
                wEH[i][j] = Math.random() * 0.2 + 0.1;
            }
        }
        for (int i = 0; i < hidden.length; i++) {
            for (int j = 0; j < out.length; j++) {
                wHO[i][j] = Math.random() * 0.2 + 0.1;
            }
        }
    }

    private void countOut() {
        for (int i = 0; i < hidden.length; i++) {
            for (int j = 0; j < enters.length; j++) {
                hidden[i] += enters[j] * wEH[j][i];
            }
            hidden[i] = 1 / (1 + Math.exp(-alpha * hidden[i]));
        }

        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < hidden.length; j++) {
                out[i] += hidden[j] * wHO[j][i];
            }
            out[i] = 1 / (1 + Math.exp(-alpha * out[i]));
        }
    }

    public void setEnters(double[] enters) {
        this.enters = enters;
    }

    public double[] getResult() {
        this.countOut();

        double[] result = new double[out.length];

        for (int i = 0; i < out.length; i++) {
            result[i] = out[i] > 0.5 ? 1 : 0;
        }

        return result;
    }

    public void printOut() {

        for (int i = 0; i < out.length; i++) {
            System.out.print(out[i] > 0.5 ? 1 + " " : 0 + " ");
        }
        System.out.println();
    }

    public void learn() {
        double error = 0;
        int iterations = 0;
        double[] delta_k = new double[out.length];
        double[] delta_j = new double[hidden.length];

        do {
            for (int p = 0; p < patterns.length; p++) {
                System.arraycopy(patterns[p], 0, enters, 0, enters.length);
                countOut();

                for (int i = 0; i < out.length; i++) {
                    delta_k[i] = out[i] * (1 - out[i]) * (out[i] - answers[p][i]);
                }

                error = 0;

                for (int i = 0; i < delta_k.length; i++) {
                    error += delta_k[i] * delta_k[i];
                }

                error /= 2;

                for (int i = 0; i < hidden.length; i++) {
                    double sum = 0;
                    for (int j = 0; j < out.length; j++) {
                        sum += delta_k[j] * wHO[i][j];
                    }
                    delta_j[i] = hidden[i] * (1 - hidden[i]) * sum;
                }

                for (int i = 0; i < enters.length; i++) {
                    for (int j = 0; j < hidden.length; j++) {
                        wEH[i][j] += -0.01 * delta_j[j] * enters[i];
                    }
                }

                for (int i = 0; i < hidden.length; i++) {
                    for (int j = 0; j < out.length; j++) {
                        wHO[i][j] += -0.01 * delta_k[j] * hidden[i];
                    }
                }
            }
            iterations++;
            if (iterations % 10 == 0) System.out.println(iterations + " " + error);
        } while (error > 0.001);
    }
}