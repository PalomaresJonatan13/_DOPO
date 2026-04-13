package domain;

public class FifaUtils {
    public static boolean isInteger(String string) {
        boolean isInteger_ = true;
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            isInteger_ = false;
        }
        return isInteger_;
    }

    /* public static boolean isNaturalNumber(String string) {
        boolean isNaturalNumber_ = true;
        try {
            int integer = Integer.parseInt(string);
            isNaturalNumber_ = integer >= 0;
        } catch (NumberFormatException e) {
            isNaturalNumber_ = false;
        }
        return isNaturalNumber_;
    } */
}
