package unfpa;

import java.util.Hashtable;
import java.util.Enumeration;

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

    public static String[] villages() {
        Configuration config = new Configuration();
        String district = config.get("district");
        String[] none = {""};
        return none;
    }

    public static Hashtable hash_district() {
        Hashtable districts = new Hashtable();
        districts.put("oule", "Ouelessebougou");
        districts.put("kati", "Kati");
        districts.put("kkr", "Koulikoro");
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
}
