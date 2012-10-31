package unfpa;

import java.util.Hashtable;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 * @author reg
 */


public class StaticCodes {

    public Hashtable cercles = new Hashtable();
    public Hashtable names = new Hashtable();

    public StaticCodes() {

        // COMMUNES HASH TABLES
        // contains a list of vil_code/vil_name for each village in commune.

        // Markala
        Hashtable m105_ht = new Hashtable();
        m105_ht.put("v01949", "Bambougou");
        m105_ht.put("v01950", "Bambougounikoro");
        m105_ht.put("v01951", "Binatomabougou");
        m105_ht.put("v01952", "Diamouna");
        m105_ht.put("v01953", "Dougouba");
        m105_ht.put("v01954", "Faya");

        // Niono
        Hashtable m128_ht = new Hashtable();
        m128_ht.put("v02360", "B4 1-2");
        m128_ht.put("v02361", "Foabougou");
        m128_ht.put("v02362", "Kala-Nampala");
        m128_ht.put("v02363", "Kolodougou Coro");
        m128_ht.put("v02364", "Kolodougou-Coura");
        m128_ht.put("v02365", "Korndobougou");


        // Fake
        Hashtable m009_ht = new Hashtable();
        m009_ht.put("v11949", "UBambougou");
        m009_ht.put("v11950", "UBambougounikoro");
        m009_ht.put("v11951", "UBinatomabougou");
        m009_ht.put("v11952", "UDiamouna");
        m009_ht.put("v11953", "UDougouba");
        m009_ht.put("v11954", "UFaya");

        // Fake
        Hashtable m010_ht = new Hashtable();
        m010_ht.put("v12360", "UB4 1-2");
        m010_ht.put("v12361", "UFoabougou");
        m010_ht.put("v12362", "UKala-Nampala");
        m010_ht.put("v12363", "UKolodougou Coro");
        m010_ht.put("v12364", "UKolodougou-Coura");
        m010_ht.put("v12365", "UKorndobougou");

        // CERCLES HASH TABLES
        // contains a list of com_code/com_ht for each commune in cercle.

        // Ségou
        Hashtable csego_ht = new Hashtable();
        csego_ht.put("m105", m105_ht);
        csego_ht.put("m128", m128_ht);

        // Barouéli
        Hashtable cbara_ht = new Hashtable();
        cbara_ht.put("m010", m010_ht);
        cbara_ht.put("m009", m009_ht);

        // MAIN HASH TABLE
        // contains a list of cercle_code/cercle_ht for each cercle.

        cercles.put("csego", csego_ht);
        cercles.put("cbara", cbara_ht);

        // NAMES HASH TABLE
        // contains names of code/name for all communes and cercles
        names.put("m105", "Markala");
        names.put("m128", "Niono");
        names.put("m010", "Barouéli");
        names.put("m009", "Nara");
        names.put("csego", "Ségou");
        names.put("cbara", "Barouéli");

    }

}