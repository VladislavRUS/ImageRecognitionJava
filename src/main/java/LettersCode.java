/**
 * Created by User on 012 12.12.16.
 */
public class LettersCode {
    private static final String LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static double[] getLetterCode(String letter) {

        byte[] bytes = letter.getBytes();
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            int val = b;

            for (int j = 0; j < 8; j++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }

        double[] result = new double[binary.length()];

        for (int i = 0; i < binary.length(); i++) {
            result[i] = Integer.parseInt(Character.toString(binary.charAt(i)));
        }

        return result;
    }

    public static String getLetterByCode(double[] code) {
        for (int i = 0; i < LETTERS.length(); i++) {
            double[] c = getLetterCode(Character.toString(LETTERS.charAt(i)));
            if (isArrayEqual(c, code)) {
                return Character.toString(LETTERS.charAt(i));
            }
        }
        System.out.println("FUCKINH SHIT");
        System.out.println(code.length);
        return "haha";
    }

    public static boolean isArrayEqual(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) throw new RuntimeException("Not equal!");

        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }

        return true;
    }
}
