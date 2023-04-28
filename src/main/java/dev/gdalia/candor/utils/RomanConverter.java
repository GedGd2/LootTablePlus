package dev.gdalia.candor.utils;

public class RomanConverter {

    public static String convert(int number) {
        int[] val = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        String[] romans = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < val.length; i++) {
            while (number >= val[i]) {
                number -= val[i];
                builder.append(romans[i]);
            }
        }

        return builder.toString();
    }
}
