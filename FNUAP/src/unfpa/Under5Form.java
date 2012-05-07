
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
    private TextField other;
    private ChoiceGroup location;
    private DateField reporting_date;
    private TextField reporting_location;
    private TextField death_location;
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

        reporting_date =  new DateField("Date de visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(now);

        reporting_location = new TextField("Code village (visite):", null, Constants.LOC_CODE_MAX, TextField.ANY);

        name = new TextField("Nom de l'enfant", null, 20, TextField.ANY);

        dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dob.setDate(now);

        age =  new TextField("Age (DDN inconnue):", null, Constants.AGE_STR_MAX, TextField.ANY);
        sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
        location = new ChoiceGroup("Lieu de Naissance:", ChoiceGroup.POPUP, TypeLocation, null);
        other = new TextField("Précision", null, 20, TextField.ANY);
        dod =  new DateField("Date du décès:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dod.setDate(now);

        death_location = new TextField("Code village (décès):", null, Constants.LOC_CODE_MAX, TextField.ANY);

        append(reporting_date);
        append(reporting_location);
        append(name);
        append(sex);
        append(age);
        append(dob);
        append(dod);
        append(death_location);
        append(location);
        append(other);

        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        if (name.getString().length() == 0||
            reporting_location.getString().length() == 0||
            death_location.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        // TODO la date visite ne doit pas être > a la date de naissance
        ErrorMessage = "La date indiquée est dans le futur.";

        if (SharedChecks.isDateValide(reporting_date.getDate()) != true) {
            ErrorMessage = "(Date repportage) " + ErrorMessage;
            return false;
        }

        if (SharedChecks.isDateValide(dod.getDate())!= true){
            
            ErrorMessage = "(Date de la mort) " + ErrorMessage;
            return false;
        }

        if (SharedChecks.Under5(dob.getDate()) == false) {
            ErrorMessage = "L'enfant doit être moins de 5ans.";
            return false;
        }

        if (SharedChecks.ValidateCode(reporting_location.getString()) == true) {
            ErrorMessage = "[Code village (visite)] ce code n'est pas valide";
            return false;
        }

        if (SharedChecks.ValidateCode(death_location.getString()) == true) {
            ErrorMessage = "[Code village (décès)] ce code n'est pas valide";
            return false;
        }

        if (SharedChecks.compareDobDod(dob.getDate(), dod.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de la mort ne peut pas être inferieure à la date de la naissance";
            return false;
        }
        String age_nbr = String.valueOf(age.getString().charAt(age.getString().length() - 1));
        
        if (!age_nbr.equals("a") && !age_nbr.equals("m")){
            ErrorMessage = "Age doit être suivi d'un 'a' pour l'année ou d'un 'm' pour le mois" ;
            return false;
        }
        return true;
    }

    public String toSMSFormat() {

        String loc;
        String fdob;
        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2]) + SharedChecks.addzero(reporting_date_array[1]) + SharedChecks.addzero(reporting_date_array[0]);

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        String dob_d = String.valueOf(dob_array[2]) + SharedChecks.addzero(dob_array[1]) + SharedChecks.addzero(dob_array[0]);

        int dod_array[] = SharedChecks.formatDateString(dod.getDate());
        String dod_d = String.valueOf(dod_array[2]) + SharedChecks.addzero(dod_array[1]) + SharedChecks.addzero(dod_array[0]);

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

        return "fnuap du5 " + sep + reporting_d 
                            + sep + reporting_location.getString()
                            + sep + name.getString()
                            + sep + sex.getString(sex.getSelectedIndex())
                            + sep + fdob
                            + sep + dod_d
                            + sep + death_location.getString()
                            + sep + loc;
    }


    public String toText() {
        return "E] " + name.getString();
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
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
