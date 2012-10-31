
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.Entities.*;
import unfpa.SharedChecks.*;
import java.util.Date;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author alou/fadiga
 */


public class BirthForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    UNFPAMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    //register
    private DateField reporting_date;
    private TextField surname_mother;
    private TextField family_name;
    private TextField surname_child;
    private ChoiceGroup reporting_locationField;
    private static final String[] sexList= {"F", "M"};
    private ChoiceGroup sex;
    private static final String[] YesNon = {"OUI", "NON"};
    private static final String[] birth_place = {"Domicile", "Centre", "Autre"};
    private ChoiceGroup born_alive;
    private ChoiceGroup birth_location;
    private DateField dob;
    
    Date now = new Date();
    String sep = " ";
    
    public BirthForm(UNFPAMIDlet midlet) {
        super("Données de naissance");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        String commune_code = config.get("commune_code");
        String old_ind_reporting = config.get("reporting_location");

        //date
        reporting_date =  new DateField("Date de visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(now);
        dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dob.setDate(now);

        //text
        surname_mother = new TextField("Prénom de la mère:", null, 20, TextField.ANY);
        family_name = new TextField("Nom de famille:", null, 20, TextField.ANY);
        surname_child = new TextField("Prénom de l'enfant:", null, 20, TextField.ANY);
        reporting_locationField = new ChoiceGroup("Code village (visite):", ChoiceGroup.POPUP, Entities.villages_names(commune_code), null);
        reporting_locationField.setSelectedIndex(Integer.parseInt(old_ind_reporting), true);
        //choice
        birth_location = new ChoiceGroup("Lieu de naissance:", ChoiceGroup.POPUP, birth_place, null);
        sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
        born_alive = new ChoiceGroup("Né vivant:", ChoiceGroup.POPUP, YesNon, null);

        append(reporting_date);
        append(reporting_locationField);
        append(family_name);
        append(surname_mother);
        append(surname_child);
        append(dob);
        append(birth_location);
        append(sex);
        append(born_alive);

        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        if (family_name.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        //TODO: Si c'est Autre qui est choisi comme lieu de naissance le change précision devient obligatoire.
        //      la date de naissance ne doit pas être superière à la date d'enregistrement.
        ErrorMessage = "La date indiquée est dans le futur.";

        if (SharedChecks.isDateValide(reporting_date.getDate()) != true) {
            ErrorMessage = "[Date de visite] " + ErrorMessage;
            return false;
        }

        if (SharedChecks.isDateValide(dob.getDate()) != true) {
            ErrorMessage = "[Date de naissance] " + ErrorMessage;
            return false;
        }
        if (SharedChecks.compareDobDod(dob.getDate(), reporting_date.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de visite ne peut pas être inferieure à la date de la naissance.";
            return false;
        }

        return true;
    }

    public String toSMSFormat() {

        String loc;
        String mother;
        String child;
        int born;

        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2])
                             + SharedChecks.addzero(reporting_date_array[1])
                             + SharedChecks.addzero(reporting_date_array[0]);

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        String dob_d = String.valueOf(dob_array[2])
                             + SharedChecks.addzero(dob_array[1])
                             + SharedChecks.addzero(dob_array[0]);

        if (birth_location.getString(birth_location.getSelectedIndex()).equals("Domicile"))
            loc = "D";
        else if (birth_location.getString(birth_location.getSelectedIndex()).equals("Centre"))
            loc = "C";
        else
            loc = "A";

        if (born_alive.getString(born_alive.getSelectedIndex()).equals("OUI"))
            born = 1;
        else
            born = 0;

        if (surname_mother.getString().length() == 0)
            mother = "-";
        else
            mother = surname_mother.getString();

        if (surname_child.getString().length() == 0)
            child = "-";
        else
            child = surname_child.getString();

        String prof = SharedChecks.profile();
        String commune_code = config.get("commune_code");

        
        String reporting_location_index = String.valueOf(reporting_locationField.getSelectedIndex());
        
        // On sauvegarde l'index pour l'ulitiser par defaut après        
        config.set("reporting_location", reporting_location_index);

        return "fnuap born" + sep + prof + sep + reporting_d
                            + sep + Entities.villages_codes(commune_code)[reporting_locationField.getSelectedIndex()]
                            + sep + family_name.getString().replace(' ', '_')
                            + sep + mother.replace(' ', '_')
                            + sep + child.replace(' ', '_')
                            + sep + dob_d
                            + sep + loc
                            + sep + sex.getString(sex.getSelectedIndex())
                            + sep + born;
    }

    public String toText() {
        int dob_array[] = SharedChecks.formatDateString(dob.getDate());

        return "N-" + dob_array[0] + "] " + family_name.getString();
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "born");
            this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command
        if (c == CMD_SAVE) {
            Alert alert;

            if (!this.isComplete()) {
                alert = new Alert("Données manquantes", "Tous les champs " +
                                  "requis doivent être remplis!", null,
                                   AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            if (!this.isValid()) {
                alert = new Alert("Données incorrectes!", this.ErrorMessage,
                                  null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // sends the sms and reply feedback
            SMSSender sms = new SMSSender();
            String number = config.get("server_number");
            if (sms.send(number, this.toSMSFormat())) {
                alert = new Alert ("Demande envoyée !", "Vous allez recevoir" +
                                   " une confirmation du serveur.",
                                   null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                // TODO: ajouter sauvegarde dans BDD.

               if (store.add(this.toText(), this.toSMSFormat())) {
                    alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                                       " la demande par SMS. Le rapport a été enregistré dans le téléphone.", null,
                                       AlertType.WARNING);
                } else {
                    alert = new Alert ("Échec d'enregistrement", "Impossible d'envoyer ni d'enregistrer dans le téléphone.", null,
                                       AlertType.WARNING);
                }
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            }

        }
    }
}
