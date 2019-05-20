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

        reasons.put("1", "Allgemeine Frage");
        reasons.put("2", "Bewerbungen");
        reasons.put("3", "Report / Entbannung");
        reasons.put("4", "Bug melden");
        reasons.put("5", "Beschwerde / Administrationsgespräch");

    }

    public Map<String, String> getReasons() {
        return reasons;
    }
}
