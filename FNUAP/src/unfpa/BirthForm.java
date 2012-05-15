
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import unfpa.Configuration.*;
import unfpa.Constants.*;
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
    private TextField householder;
    private TextField name_mother;
    private TextField name_father;
    private TextField name_child;
    private TextField reporting_location;
    private static final String[] sexList= {"F", "M"};
    private ChoiceGroup sex;
    private static final String[] YesNon = {"OUI", "NON"};
    private static final String[] TypeLocation = {"Domicile", "Centre", "Autre"};
    private TextField other;
    private ChoiceGroup born_alive;
    private ChoiceGroup location;
    private DateField dob;
    private TextField age;
    Date now = new Date();
    String sep = " ";

    public BirthForm(UNFPAMIDlet midlet) {
        super("Données de naissance");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        //date
        reporting_date =  new DateField("Date d'enregistrement", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(now);
        dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dob.setDate(now);

        //text
        householder = new TextField("Chef de menage", null, 20, TextField.ANY);
        name_mother = new TextField("Nom de la mère", null, 20, TextField.ANY);
        name_father = new TextField("Nom du père", null, 20, TextField.ANY);
        name_child = new TextField("Nom de l'enfant", null, 20, TextField.ANY);
        other = new TextField("Précision", null, 20, TextField.ANY);
        age =  new TextField("Age (DDN inconnue):", null, Constants.AGE_STR_MAX, TextField.ANY);
        reporting_location = new TextField("Code village (visite):", null, Constants.LOC_CODE_MAX, TextField.ANY);

        //choice
        location = new ChoiceGroup("Lieu de naissance:", ChoiceGroup.POPUP, TypeLocation, null);
        sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
        born_alive = new ChoiceGroup("Né vivant:", ChoiceGroup.POPUP, YesNon, null);

        append(reporting_date);
        append(reporting_location);
        append(householder);
        append(name_father);
        append(name_mother);
        append(name_child);
        append(age);
        append(dob);
        append(location);
        append(other);
        append(sex);
        append(born_alive);

        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        if (reporting_location.getString().length() == 0
            || householder.getString().length() == 0
            || name_father.getString().length() == 0
            || name_mother.getString().length() == 0
            || name_child.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        //TODO: Si c'est Autre qui est choisi comme lieu de naissance le change précision devient obligatoire.
        //      la date de naissance ne doit pas être superière à la date d'enregistrement.
        ErrorMessage = "La date indiquée est dans le futur.";

        if (SharedChecks.isDateValide(reporting_date.getDate()) != true) {
            ErrorMessage = "[Date repportage] " + ErrorMessage;
            return false;
        }

        if (SharedChecks.isDateValide(dob.getDate()) != true) {
            ErrorMessage = "[Date repportage] " + ErrorMessage;
            return false;
        }
        if (SharedChecks.compareDobDod(dob.getDate(), reporting_date.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de visite ne peut pas être inferieure à la date de la naissance";
            return false;
        }
        if (SharedChecks.ValidateCode(reporting_location.getString()) == true) {
            ErrorMessage = "[Code village (visite)] ce code n'est pas valide";
            return false;
        }
        
        if (age.getString().length()!= 0){
            String age_nbr = String.valueOf(age.getString().charAt(age.getString().length() - 1));

            if (!age_nbr.equals("a") && !age_nbr.equals("m")){
                ErrorMessage = "Le nombre d'age doit être suivi d'un 'a' pour l'année ou d'un 'm' pour le mois" ;
                return false;
            }
        }

        if (location.getString(location.getSelectedIndex()).equals("Autre") && other.getString().length() == 0){
                ErrorMessage = "Precisez le lieu de naissance " ;
                return false;
            }
        
        return true;
    }

    public String toSMSFormat() {

        String fdob;
        String loc;
        int born;

        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2]) 
                             + SharedChecks.addzero(reporting_date_array[1]) 
                             + SharedChecks.addzero(reporting_date_array[0]);

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        String dob_d = String.valueOf(dob_array[2]) 
                             + SharedChecks.addzero(dob_array[1]) 
                             + SharedChecks.addzero(dob_array[0]);

        if (age.getString().length() != 0)
            fdob = age.getString();
        else
            fdob = dob_d;

        if (location.getString(location.getSelectedIndex()).equals("Domicile"))
            loc = "D";
        else if (location.getString(location.getSelectedIndex()).equals("Centre"))
            loc = "C";
        else
            loc = other.getString();

        if (born_alive.getString(born_alive.getSelectedIndex()).equals("OUI"))
            born = 1;
        else
            born = 0;

        return "fnuap born" + sep + reporting_d
                            + sep + reporting_location.getString()
                            + sep + householder.getString().replace(' ', '_')
                            + sep + name_father.getString().replace(' ', '_')
                            + sep + name_mother.getString().replace(' ', '_')
                            + sep + name_child.getString().replace(' ', '_')
                            + sep + fdob
                            + sep + loc
                            + sep + sex.getString(sex.getSelectedIndex())
                            + sep + born;
    }

    public String toText() {

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        int dob_day = dob_array[0];
        int dob_month = dob_array[1];

        return "N] " + householder.getString() + dob_day + "/" + dob_month;
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
