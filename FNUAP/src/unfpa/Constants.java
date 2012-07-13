package unfpa;

import java.util.Hashtable;
import java.util.Enumeration;
import javax.microedition.lcdui.ChoiceGroup;

/**
 * Static constant holder
 * @author fadiga
 */
public class Constants {

    public static final String version = "0.5";
    public static final String server_number = "65731076";
    public static final int LOC_CODE_MAX = 4;
    public static final int AGE_STR_MAX = 3;
    public Hashtable districts;

    private static final String CODE_OULESSEBOUGOU = "oule";
    private static final String CODE_KATI = "kati";
    private static final String CODE_KOULIKORO = "kkr";

    public static Hashtable hash_district() {
        Hashtable districts = new Hashtable();
        districts.put(CODE_OULESSEBOUGOU, "Ouelessebougou");
        districts.put(CODE_KATI, "Kati");
        districts.put(CODE_KOULIKORO, "Koulikoro");
        return districts;
    }

    public static String[] codes_district() {
        Hashtable dis_hash = Constants.hash_district();
        int num = dis_hash.size();
        String[] codes = new String[num];
        int i = 0;

        for(Enumeration dis = dis_hash.keys(); dis.hasMoreElements();) {
            String dis_code = (String)dis.nextElement();
            codes[i] = dis_code;
            i++;
        }
        return codes;
    }

    public static String[] names_district() {
        Hashtable dis_hash = Constants.hash_district();
        int num = dis_hash.size();
        String[] names = new String[num];
        int i = 0;
        for(Enumeration dis = dis_hash.keys(); dis.hasMoreElements();) {
            String dis_code = (String)dis.nextElement();
            String dis_name = (String)dis_hash.get(dis_code);
            names[i] = dis_name;
            i++;
        }
        return names;
    }

    public static String code_for_district(ChoiceGroup widget) {
        return codes_district()[widget.getSelectedIndex()];
    }

    public static Hashtable hash_village() {
        // if no district_code provided, used stored one (config)
        Configuration config = new Configuration();
        String district_code = config.get("district_code");
        return hash_village(district_code);
    }

    public static Hashtable hash_village(String district_code) {
        // static manual list of villages by district
        Hashtable kati = new Hashtable();
        kati.put("kat1", "Kati marché");
        kati.put("kat2", "Kati Cocody");
        kati.put("kat3", "Kati koro");

        Hashtable kkr = new Hashtable();
        kkr.put("kkr1", "KKR marché");
        kkr.put("kkr2", "KKR Cocody");
        kkr.put("kkr3", "KKR koro");

        Hashtable oule = new Hashtable();
        oule.put("oul1", "Ouele marché");
        oule.put("oul2", "Ouele Cocody");
        oule.put("oul3", "Ouele koro");

        // match above ist with districts.
        Hashtable districts = new Hashtable();
        districts.put(CODE_OULESSEBOUGOU, oule);
        districts.put(CODE_KATI, kati);
        districts.put(CODE_KOULIKORO, kkr);

        return (Hashtable)districts.get(district_code);
    }

    public static String[] codes_village() {
        // if no district_code provided, used stored one (config)
        Configuration config = new Configuration();
        String district_code = config.get("district_code");
        return codes_village(district_code);
    }

    public static String[] codes_village(String district_code) {

        Hashtable vil_hash = Constants.hash_village(district_code);
        int num = vil_hash.size();
        String[] codes = new String[num];
        int i = 0;

        for(Enumeration vil = vil_hash.keys(); vil.hasMoreElements();) {
            String vil_code = (String)vil.nextElement();
            codes[i] = vil_code;
            i++;
        }
        return codes;
    }

    public static String[] names_village() {
        // if no district_code provided, used stored one (config)
        Configuration config = new Configuration();
        String district_code = config.get("district_code");
        return names_village(district_code);
    }

    public static String[] names_village(String district_code) {
        // if no district_code provided, used stored one (config)
        if (district_code.equals("")) {
            Configuration config = new Configuration();
            district_code = config.get("district_code");
        }

        Hashtable vil_hash = Constants.hash_village(district_code);
        int num = vil_hash.size();
        String[] names = new String[num];
        int i = 0;
        for(Enumeration vil = vil_hash.keys(); vil.hasMoreElements();) {
            String vil_code = (String)vil.nextElement();
            String vil_name = (String)vil_hash.get(vil_code);
            names[i] = vil_name;
            i++;
        }
        return names;
    }

    public static String code_for_village(ChoiceGroup widget) {
        // if no district_code provided, used stored one (config)
        Configuration config = new Configuration();
        String district_code = config.get("district_code");
        return code_for_village(widget, district_code);
    }

    public static String code_for_village(ChoiceGroup widget, String district_code) {
        return codes_village(district_code)[widget.getSelectedIndex()];
    }
}
