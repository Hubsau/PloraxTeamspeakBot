package de.hubsau.ploraxteamspeakbot.listener;
/*Class erstellt von Hubsau


20:49 2019 08.05.2019
Wochentag : Mittwoch


*/


import java.util.HashMap;
import java.util.Map;

public class  SupportReason {

    Map<String, String> reasons = new HashMap<>();

    public SupportReason() {

        reasons.put("1", "AlFr");
        reasons.put("2", "BW");
        reasons.put("3", "Rt/Eb");
        reasons.put("4", "Bug");
        reasons.put("5", "B/AG");

    }

    public Map<String, String> getReasons() {
        return reasons;
    }
}
