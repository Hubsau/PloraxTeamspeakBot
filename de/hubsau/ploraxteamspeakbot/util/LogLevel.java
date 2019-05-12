package de.hubsau.ploraxteamspeakbot.util;
/*Class erstellt von Hubsau


14:08 2019 27.01.2019
Wochentag : Sonntag


*/


public enum LogLevel {


    DEBUG(0, "DEBUG"), INFORMATION(1, "INFORMATION"), WARNING(2, "WARNING"), ERROR(3, "ERROR"), CRITICAL(4, "CRITICAL");


    private int level;
    private String name;

    LogLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
