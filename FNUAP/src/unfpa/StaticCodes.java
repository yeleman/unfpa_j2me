
package unfpa;

import java.util.Hashtable;

import unfpa.StaticCodescsego.*;
import unfpa.StaticCodescsan.*;
import unfpa.StaticCodescbara.*;
import unfpa.StaticCodescbla.*;
import unfpa.StaticCodesctomi.*;
import unfpa.StaticCodescmaci.*;
import unfpa.StaticCodescnion.*;
import unfpa.StaticCodescnara.*;
import unfpa.StaticCodesckati.*;
import unfpa.StaticCodesckang.*;
import unfpa.StaticCodescbana.*;
import unfpa.StaticCodesckolo.*;
import unfpa.StaticCodesckoul.*;
import unfpa.StaticCodescdioi.*;


/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 * @author reg
 */


public class StaticCodes {

    public Hashtable cercles = new Hashtable();
    public Hashtable names = new Hashtable();

    public StaticCodes() {

        // MAIN HASH TABLE
        // contains a list of cercle_code/cercle_ht for each cercle.
		cercles.put("csego", StaticCodescsego.ht()); // Ségou
		cercles.put("csan", StaticCodescsan.ht()); // San
		cercles.put("cbara", StaticCodescbara.ht()); // Barouéli
		cercles.put("cbla", StaticCodescbla.ht()); // Bla
		cercles.put("ctomi", StaticCodesctomi.ht()); // Tominian
		cercles.put("cmaci", StaticCodescmaci.ht()); // Macina
		cercles.put("cnion", StaticCodescnion.ht()); // Niono
		cercles.put("cnara", StaticCodescnara.ht()); // Nara
		cercles.put("ckati", StaticCodesckati.ht()); // Kati
		cercles.put("ckang", StaticCodesckang.ht()); // Kangaba
		cercles.put("cbana", StaticCodescbana.ht()); // Banamba
		cercles.put("ckolo", StaticCodesckolo.ht()); // Kolokani
		cercles.put("ckoul", StaticCodesckoul.ht()); // Koulikoro
		cercles.put("cdioi", StaticCodescdioi.ht()); // Dioila

        // NAMES HASH TABLE
        // contains names of code/name for all communes and cercles
		names.put("csego", "Ségou");
		names.put("m142", "Pelengana");
		names.put("m047", "Diouna");
		names.put("m105", "Markala");
		names.put("m186", "Togou");
		names.put("m026", "Cinzana");
		names.put("m172", "Souba");
		names.put("m157", "Sansanding");
		names.put("m148", "Sama Foulala");
		names.put("m167", "Soignebougou");
		names.put("m130", "N'Koumandougou");
		names.put("m159", "Sebougou");
		names.put("m106", "Massala");
		names.put("m054", "Dougabougou");
		names.put("m210", "Doura");
		names.put("m046", "Dioro");
		names.put("m132", "Nonongo");
		names.put("m011", "Bellen");
		names.put("m021", "Boussin");
		names.put("m066", "Fatine");
		names.put("m146", "Sakoiba");
		names.put("m037", "Diedougou 1");
		names.put("m064", "Farako 2");
		names.put("m041", "Diganibougou");
		names.put("m207", "Segou");
		names.put("m065", "Farakou Massa");
		names.put("m162", "Sibila");
		names.put("m002", "Baguindadougou");
		names.put("m090", "Konodimini");
		names.put("m082", "Katiena");
		names.put("csan", "San");
		names.put("m174", "Sourountouna");
		names.put("m161", "Siadougou");
		names.put("m170", "Somo L");
		names.put("m177", "Tene");
		names.put("m175", "Sy");
		names.put("m113", "Moribila");
		names.put("m025", "San");
		names.put("m192", "Tourakolomba");
		names.put("m122", "N'Goa");
		names.put("m032", "Diakourouna");
		names.put("m178", "Teneni");
		names.put("m048", "Djeguena");
		names.put("m135", "N'Torosso");
		names.put("m008", "Baramandougou");
		names.put("m081", "Kassorola");
		names.put("m193", "Waki");
		names.put("m039", "Dieli");
		names.put("m078", "Kaniegue");
		names.put("m083", "Kava");
		names.put("m067", "Fion");
		names.put("m080", "Karaba");
		names.put("m127", "Niasso");
		names.put("m029", "Dah");
		names.put("m141", "Ouolon");
		names.put("m125", "Niamana 1");
		names.put("cbara", "Barouéli");
		names.put("m069", "Gouendo");
		names.put("m176", "Tamani");
		names.put("m089", "Konobougou");
		names.put("m075", "Kalake");
		names.put("m152", "Sanando");
		names.put("m179", "Tesserla");
		names.put("m121", "N'Gassola");
		names.put("m171", "Somo Ll");
		names.put("m009", "Baraoueli");
		names.put("m016", "Boidie");
		names.put("m120", "N'Gara");
		names.put("m150", "Samine");
		names.put("m055", "Dougoufie");
		names.put("cbla", "Bla");
		names.put("m085", "Kemeni");
		names.put("m010", "Beguene");
		names.put("m062", "Fani");
		names.put("m149", "Samabogo");
		names.put("m124", "Niala");
		names.put("m056", "Dougouolo");
		names.put("m035", "Diaramana");
		names.put("m182", "Tiemena");
		names.put("m015", "Bla");
		names.put("m060", "Falo");
		names.put("m091", "Korodougou");
		names.put("m194", "Yangasso");
		names.put("m191", "Touna");
		names.put("m084", "Kazangasso");
		names.put("m040", "Diena 2");
		names.put("m095", "Koulandougou");
		names.put("m169", "Somasso");
		names.put("ctomi", "Tominian");
		names.put("m093", "Koula 1");
		names.put("m184", "Timissa");
		names.put("m097", "Lanfiala");
		names.put("m187", "Tominian");
		names.put("m100", "Mafoune");
		names.put("m102", "Mandiakuy");
		names.put("m138", "Ouan");
		names.put("m013", "Benena");
		names.put("m045", "Diora");
		names.put("m061", "Fangasso");
		names.put("m195", "Yasso");
		names.put("m155", "Sanekuy");
		names.put("cmaci", "Macina");
		names.put("m151", "Sana");
		names.put("m108", "Matomo");
		names.put("m212", "Kolongotomo");
		names.put("m088", "Kolongo");
		names.put("m086", "Kokry");
		names.put("m068", "Folomana");
		names.put("m173", "Souleye");
		names.put("m188", "Tongue");
		names.put("m098", "Macina");
		names.put("m111", "Monimpebougou");
		names.put("m147", "Saloba");
		names.put("m213", "Monimpe");
		names.put("m017", "Boky Were");
		names.put("m211", "Saye");
		names.put("cnion", "Niono");
		names.put("m143", "Pogo");
		names.put("m104", "Mariko");
		names.put("m073", "Kala Siguida");
		names.put("m051", "Dogofry");
		names.put("m197", "Yeredon");
		names.put("m030", "Diabaly");
		names.put("m117", "Nampalari");
		names.put("m168", "Sokolo");
		names.put("m165", "Siribala");
		names.put("m128", "Niono");
		names.put("m020", "Boundy");
		names.put("m189", "Toridaga-Ko");
		names.put("cnara", "Nara");
		names.put("m050", "Dogofry");
		names.put("m137", "Ouagadou");
		names.put("m001", "Allahina");
		names.put("m059", "Fallou");
		names.put("m092", "Koronga");
		names.put("m042", "Dilly");
		names.put("m070", "Gueneibe");
		names.put("m118", "Nara");
		names.put("m126", "Niamana 2");
		names.put("m072", "Guire");
		names.put("m028", "Dabo");
		names.put("ckati", "Kati");
		names.put("m181", "Tiele");
		names.put("m196", "Yelekebougou");
		names.put("m053", "Doubabougou");
		names.put("m038", "Diedougou 3");
		names.put("m063", "Faraba 1");
		names.put("m003", "Baguineda Camp");
		names.put("m119", "N'Gabacoro");
		names.put("m208", "Baguineda");
		names.put("m031", "Diago");
		names.put("m154", "Sanankoroba");
		names.put("m019", "Bougoula 1");
		names.put("m101", "Mande");
		names.put("m112", "Moribabougou");
		names.put("m007", "Bancoumana");
		names.put("m005", "Bamako");
		names.put("m096", "Kourouba");
		names.put("m076", "Kalifabougou");
		names.put("m114", "Mountougoula");
		names.put("m074", "Kalabancoro");
		names.put("m022", "Kati");
		names.put("m052", "Dombila");
		names.put("m044", "Dio Gare");
		names.put("m077", "Kambila");
		names.put("m018", "Bossofla");
		names.put("m153", "Sanankoro Djitoumou");
		names.put("m034", "Dialakorodji");
		names.put("m144", "Safo");
		names.put("m027", "Daban");
		names.put("m049", "Dogodouman");
		names.put("m163", "Siby");
		names.put("m115", "N'Gouraba");
		names.put("m116", "N'Tjiba");
		names.put("m123", "Niagadina");
		names.put("m033", "Dialakoroba");
		names.put("m180", "Tiakadougou Dialakoro");
		names.put("m139", "Ouelessebougou");
		names.put("m129", "Nioumamakana");
		names.put("m156", "Sangarebougou");
		names.put("m166", "Sobra");
		names.put("ckang", "Kangaba");
		names.put("m110", "Minidian");
		names.put("m014", "Benkadi 1");
		names.put("m160", "Selefougou");
		names.put("m079", "Kaniogo");
		names.put("m134", "Nouga");
		names.put("m004", "Balan Bakama");
		names.put("m023", "Narena");
		names.put("m103", "Maramandougou");
		names.put("cbana", "Banamba");
		names.put("m199", "Toukoroba");
		names.put("m006", "Banamba");
		names.put("m099", "Madina Sacko");
		names.put("m205", "Boron");
		names.put("m058", "Duguwolowula");
		names.put("m200", "Sebete");
		names.put("ckolo", "Kolokani");
		names.put("m140", "Ouolodo");
		names.put("m071", "Gui Hoyo");
		names.put("m036", "Didieni");
		names.put("m087", "Kolokani");
		names.put("m107", "Massantola");
		names.put("m158", "Sebecoro");
		names.put("m131", "Nonkon");
		names.put("m012", "Ben Kadi");
		names.put("m145", "Sagabala");
		names.put("m185", "Tioribougou");
		names.put("m133", "Nossombougou");
		names.put("ckoul", "Koulikoro");
		names.put("m094", "Koula 2");
		names.put("m190", "Tougouni");
		names.put("m183", "Tienfala");
		names.put("m043", "Dinandougou 1");
		names.put("m209", "Kenekou");
		names.put("m024", "Koulikoro");
		names.put("m109", "Meguetan");
		names.put("m136", "Nyamina");
		names.put("m164", "Sirakorola");
		names.put("m057", "Doumba");
		names.put("cdioi", "Dioila");
		names.put("m204", "Banco");
		names.put("m198", "Massigui");
		names.put("m203", "Beleko");
		names.put("m202", "Fana");
		names.put("m206", "Mena");
		names.put("m201", "Dioila");

    }

}
