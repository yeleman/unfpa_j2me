package unfpa;

import java.util.Hashtable;
import java.util.Enumeration;
import unfpa.StaticCodes.*;

/**
 * StaticCodes Management
 * @author reg
 */

public class Entities {

    public Entities() {
        
    }


    public static String[] cercles_codes() {
        StaticCodes static_codes = new StaticCodes();
        int num = static_codes.cercles.size();
        String[] codes = new String[num];
        int i = 0;

        for(Enumeration cercle = static_codes.cercles.keys(); cercle.hasMoreElements();) {
            String cercle_code = (String)cercle.nextElement();
            codes[i] = cercle_code;
            i++;
        }
        return codes;
    }
    
    public static String[] cercles_names() {
        StaticCodes static_codes = new StaticCodes();

        String[] codes = Entities.cercles_codes();
        String[] names = new String[codes.length];
        for (int i=0; i < codes.length; i++) {
            String cercle_name = (String)static_codes.names.get(codes[i]);
            names[i] = cercle_name;
        }
        return names;
    }


    public static String[] communes_codes(String cercle_code) {
        StaticCodes static_codes = new StaticCodes();

        Hashtable target_ht = (Hashtable)static_codes.cercles.get(cercle_code);
        int num = target_ht.size();
        String[] codes = new String[num];
        int i = 0;

        for(Enumeration commune = target_ht.keys(); commune.hasMoreElements();) {
            String commune_code = (String)commune.nextElement();
            codes[i] = commune_code;
            i++;
        }
        return codes;
    }

    public static String[] communes_names(String cercle_code) {
        StaticCodes static_codes = new StaticCodes();

        String[] codes = Entities.communes_codes(cercle_code);
        String[] names = new String[codes.length];
        for (int i=0; i < codes.length; i++) {
            String commune_name = (String)static_codes.names.get(codes[i]);
            names[i] = commune_name;
        }
        return names;
    }

    public static String[] villages_info(String commune_code, boolean as_names) {
        StaticCodes static_codes = new StaticCodes();
        
        // loop on cercle to find which contains commune
        for(Enumeration cercle = static_codes.cercles.keys(); cercle.hasMoreElements();) {
            String cercle_code = (String)cercle.nextElement();
            Hashtable cercle_ht = (Hashtable)static_codes.cercles.get(cercle_code);
            
            if (cercle_ht.containsKey(commune_code)) {


                Hashtable target_ht = (Hashtable)cercle_ht.get(commune_code);
                int num = target_ht.size();
                String[] outputs = new String[num];
                int i = 0;

                for(Enumeration output = target_ht.keys(); output.hasMoreElements();) {
                    String output_code = (String)output.nextElement();
                    if (as_names == true) {
                        String output_name = (String)target_ht.get(output_code);
                        outputs[i] = output_name;
                    }                    
                    else {
                        outputs[i] = output_code;
                    }
                    i++;
                }
                return outputs;

            }
        }
        String[] not_found = new String[0];
        return not_found;
    }


    public static String[] villages_codes(String commune_code) {
        return Entities.villages_info(commune_code, false);
    }

    public static String[] villages_names(String commune_code) {
        return Entities.villages_info(commune_code, true);
    }

}
