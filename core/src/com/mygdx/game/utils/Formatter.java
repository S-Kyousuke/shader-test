package com.mygdx.game.utils;

public class Formatter {

    private Formatter() {
    }

    public static String decimal(float value, int places) {
        return trimDecimalPlaces(Float.toString(value), places);
    }

    public static String decimal(double value, int places) {
        return trimDecimalPlaces(Double.toString(value), places);
    }

    private static String trimDecimalPlaces(String decimalString, int places) {
        final int dotIndex = decimalString.indexOf('.');
        final StringBuilder sb = new StringBuilder(decimalString);
        if (dotIndex == -1) {
            sb.append('.');
            for (int i = 0; i < places; ++i) {
                sb.append('0');
            }
            return sb.toString();
        }
        final int endIndex = dotIndex + places + 1;
        final int stringLength = sb.length();
        for (int i = 0; i < endIndex - stringLength; ++i) {
            sb.append('0');
        }
        return sb.substring(0, endIndex);
    }


}
