/**
 * Created by User on 015 15.12.16.
 */
public class Test {
    public static void main(String[] args) {
        String letters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < letters.length(); i++) {
            String letter = Character.toString(letters.charAt(i));
            byte[] bytes = letter.getBytes();
            StringBuilder binary = new StringBuilder();

            for (byte b: bytes) {
                int val = b;

                for (int j = 0; j < 8; j++) {
                    binary.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }
                binary.append(' ');
            }
            System.out.println(binary);
        }
    }
}
