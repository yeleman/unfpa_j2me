
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

public class Under5Form extends Form implements CommandListener {

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
    private static final String[] sexList= {"F", "M"};
    private static final String[] TypeLocation = {"Domicile", "Centre", "Autre"};

    private ChoiceGroup sex;
    private ChoiceGroup location;
    private DateField reporting_date;
    private ChoiceGroup reporting_locationField;
    private ChoiceGroup death_locationField;
    private TextField name;
    private DateField dob;
    private TextField age;
    private DateField dod;
    Date now = new Date();
    String sep = " ";


    public Under5Form(UNFPAMIDlet midlet) {
        super("Mortalité infantile");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        String commune_code = config.get("commune_code");
        String old_ind_reporting = config.get("reporting_location");
        String old_ind_death = config.get("death_location");

        reporting_date =  new DateField("Date de visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(now);

        reporting_locationField = new ChoiceGroup("Code village(visite):", ChoiceGroup.POPUP, Entities.villages_names(commune_code), null);
        reporting_locationField.setSelectedIndex(Integer.parseInt(old_ind_reporting), true);

        name = new TextField("Nom de l'enfant", null, 20, TextField.ANY);

        dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dob.setDate(now);

        age =  new TextField("Age (DDN inconnue):", null, Constants.AGE_STR_MAX, TextField.ANY);
        sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
        location = new ChoiceGroup("Lieu de decès:", ChoiceGroup.POPUP, TypeLocation, null);
        dod =  new DateField("Date du décès:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dod.setDate(now);

        death_locationField =  new ChoiceGroup("Code village (décès):", ChoiceGroup.POPUP, Entities.villages_names(commune_code), null);
        death_locationField.setSelectedIndex(Integer.parseInt(old_ind_death), true);

        append(reporting_date);
        append(reporting_locationField);
        append(name);
        append(sex);
        append(age);
        append(dob);
        append(dod);
        append(death_locationField);
        append(location);

        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        if (name.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {

        ErrorMessage = "La date indiquée est dans le futur.";

        if (SharedChecks.isDateValide(reporting_date.getDate()) != true) {
            ErrorMessage = "(Date repportage) " + ErrorMessage;
            return false;
        }

        // if (SharedChecks.ValidateCode(reporting_locationField.getString()) == true) {
        //     ErrorMessage = "[Code village (visite)] ce code n'est pas valide";
        //     return false;
        // }

        if (SharedChecks.Under5(dob.getDate()) == false) {
            ErrorMessage = "[Date de naissance] l'âge de l'enfant doit être inferieur à 5ans.";
            return false;
        }

        if (age.getString().length() == 0 && SharedChecks.compareDobDod(dob.getDate(), reporting_date.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de visite ne peut pas être inferieure à la date de naissance";
            return false;
        }

        if (SharedChecks.isDateValide(dod.getDate())!= true){
            ErrorMessage = "(Date du décès) " + ErrorMessage;
            return false;
        }

        if (age.getString().length() == 0 && SharedChecks.compareDobDod(dob.getDate(), dod.getDate()) == true) {
            ErrorMessage = "[Erreur] la date du décès ne peut pas être inferieure à la date de naissance";
            return false;
        }

        if (SharedChecks.compareDobDod(dod.getDate(), reporting_date.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de visite ne peut pas être inferieure à la date du décès ";
            return false;
        }

        if (age.getString().length() != 0){
            String age_nbr = String.valueOf(age.getString().charAt(age.getString().length() - 1));

            if (!age_nbr.equals("a") && !age_nbr.equals("m")){
                ErrorMessage = "[Age (DDN inconnue)] le nombre d'âge doit être suivi d'un 'a' pour l'année ou d'un 'm' pour le mois";
                return false;
            }
        }

        return true;
    }

    public String toSMSFormat() {

        String loc;
        String fdob;

        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2])
                             + SharedChecks.addzero(reporting_date_array[1])
                             + SharedChecks.addzero(reporting_date_array[0]);

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        String dob_d = String.valueOf(dob_array[2])
                             + SharedChecks.addzero(dob_array[1])
                             + SharedChecks.addzero(dob_array[0]);

        int dod_array[] = SharedChecks.formatDateString(dod.getDate());
        String dod_d = String.valueOf(dod_array[2])
                             + SharedChecks.addzero(dod_array[1])
                             + SharedChecks.addzero(dod_array[0]);

        if (age.getString().length() != 0)
            fdob = age.getString();
        else
            fdob = dob_d;

        if (location.getString(location.getSelectedIndex()).equals("Domicile"))
            loc = "D";
        else if (location.getString(location.getSelectedIndex()).equals("Centre"))
            loc = "C";
        else
            loc = "A";

        String prof = SharedChecks.profile();
        String commune_code = config.get("commune_code");
        
        String reporting_location_index = String.valueOf(reporting_locationField.getSelectedIndex());
        String death_location_index = String.valueOf(death_locationField.getSelectedIndex());
        
        // On sauvegarde l'index pour l'ulitiser par defaut après        
        config.set("reporting_location", reporting_location_index);
        config.set("death_location", death_location_index);

        return "fnuap du5" + sep + prof + sep + reporting_d
                           + sep + Entities.villages_codes(commune_code)[reporting_locationField.getSelectedIndex()]
                           + sep + name.getString().replace(' ', '_')
                           + sep + sex.getString(sex.getSelectedIndex())
                           + sep + fdob
                           + sep + dod_d
                           + sep + Entities.villages_codes(commune_code)[death_locationField.getSelectedIndex()]
                           + sep + loc;
        
    }


    public String toText() {
        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        return "E-" + reporting_date_array[0] + "] " + name.getString();
    }


    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "under5");
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
