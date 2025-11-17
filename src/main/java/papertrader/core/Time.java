package papertrader.core;

import java.util.Map;

public class Time {

    public static Map<String, Integer> STRING_TO_MONTH = Map.ofEntries(
            Map.entry("Jan", 1),
            Map.entry("Feb", 2),
            Map.entry("Mar", 3),
            Map.entry("Apr", 4),
            Map.entry("May", 5),
            Map.entry("Jun", 6),
            Map.entry("Jul", 7),
            Map.entry("Aug", 8),
            Map.entry("Sep", 9),
            Map.entry("Oct", 10),
            Map.entry("Nov", 11),
            Map.entry("Dec", 12)
    );

    public static String monthToString(int month) {
        return switch (month) {
            case 1 -> "Jan";
            case 2 -> "Feb";
            case 3 -> "Mar";
            case 4 -> "Apr";
            case 5 -> "May";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Aug";
            case 9 -> "Sep";
            case 10 -> "Oct";
            case 11 -> "Nov";
            case 12 -> "Dec";
            default -> "";
        };
    }

    public static final Date initialDate = new Date((byte)11, (byte)10, (short)2025);
    public static final Date currentDate = new Date((byte)11, (byte)10, (short)2025);

    public static void incrementDate() {
        currentDate.day++;
        if (currentDate.day > getDaysInMonth(currentDate.month, currentDate.day)) {
            currentDate.day = 1;
            currentDate.month++;
        }
        if (currentDate.month > 12) {
            currentDate.month = 1;
            currentDate.year++;
        }
    }

    public static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    public static int getDaysInMonth(int month, int year) {
        Map<Integer, Integer> MONTHS_TO_DAYS = Map.ofEntries(
                Map.entry(1, 31),
                Map.entry(3, 31),
                Map.entry(4, 30),
                Map.entry(5, 31),
                Map.entry(6, 30),
                Map.entry(7, 31),
                Map.entry(8, 31),
                Map.entry(9, 30),
                Map.entry(10, 31),
                Map.entry(11, 30),
                Map.entry(12, 31)
        );

        // February has an extra day on leap years
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }
        if (!MONTHS_TO_DAYS.containsKey(month)) {
            System.out.println("Month " + month + " does not exist!");
            return 0;
        }
        return MONTHS_TO_DAYS.get(month);
    }

    public static class Date {
        public byte month;
        public byte day;
        public short year;

        public Date() {
            this((byte) 0,(byte) 0,(short) 0);
        }

        public Date(byte month, byte day, short year) {
            this.month = month;
            this.day = day;
            this.year = year;
        }

        public long getDaysSince(short year) {
            long inDays = ((this.year - year) * 365L) + this.day;

            for (int m = 1; m < this.month; ++m) {
                inDays += Time.getDaysInMonth(m, this.year);
            }

            return inDays - 1;
        }
    }
}
