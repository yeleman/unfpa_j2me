
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import java.util.Date;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.HelpForm.*;
import unfpa.SharedChecks.*;

/**
 * J2ME Patient Registration Form
 * Displays registration fields
 * Checks completeness
 * Sends as SMS
 * @author fad
 */

public class MaternalMortalityrForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 3);
    public UNFPAMIDlet midlet;

    private Configuration config;
    private SMSStore store;

    String sep = " ";

    private String ErrorMessage = "";
    private static final String[] pregnant = {"NON", "OUI"};
    private static final String[] pregnancy_related_death = {"NON", "OUI", "N/A"};
    //General Informatien
    private DateField reporting_date;
    private TextField reporting_location;
    // Maternal Mortality Form
    private TextField name;
    private DateField dob;
    private TextField age;
    private DateField dod;
    private TextField death_location;
    private TextField living_children;
    private TextField dead_children;
    private ChoiceGroup pregnantField;
    private TextField pregnancy_weeks;
    private ChoiceGroup pregnancy_related_deathField;


public MaternalMortalityrForm(UNFPAMIDlet midlet) {
    super("Mortalité maternelle");
    this.midlet = midlet;

    config = new Configuration();
    store = new SMSStore();

    reporting_date =  new DateField("Date de la visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    reporting_date.setDate(new Date());
    reporting_location =  new TextField("Code village (visite):", null, Constants.LOC_CODE_MAX, TextField.ANY);
    name =  new TextField("Nom de la défunte:", null, 20, TextField.ANY);
    dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    dob.setDate(new Date());
    age =  new TextField("Age (DDN inconnue):", null, Constants.AGE_STR_MAX, TextField.ANY);
    dod =  new DateField("Date du décès:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    dod.setDate(new Date());
    death_location =  new TextField("Code vilage(décès):", null, Constants.LOC_CODE_MAX, TextField.ANY);
    living_children =  new TextField("Nbre enfants (en vie):", null, 2, TextField.NUMERIC);
    dead_children =  new TextField("Nbre enfants (décédés):", null, 2, TextField.NUMERIC);
    pregnantField = new ChoiceGroup("Grossesse en cours:", ChoiceGroup.POPUP, pregnant, null);
    pregnancy_weeks =  new TextField("Nb de semaine de grossesse:", null, 3, TextField.NUMERIC);
    pregnancy_related_deathField = new ChoiceGroup("Décès lié à la grossesse:", ChoiceGroup.POPUP, pregnancy_related_death, null);

    // add fields to forms
    append(reporting_date);
    append(reporting_location);
    append(name);
    append(age);
    append(dob);
    append(dod);
    append(death_location);
    append(living_children);
    append(dead_children);
    append(pregnantField);
    append(pregnancy_weeks);
    append(pregnancy_related_deathField);

    addCommand(CMD_EXIT);
    addCommand(CMD_SEND);
    addCommand(CMD_HELP);

    this.setCommandListener (this);
}
    /*
     * Whether all required fields are filled
     * @return <code>true</code> is all fields are filled
     * <code>false</code> otherwise.
     */

    public boolean isComplete() {
        // all fields are required to be filled.
        // TODO: verifier AGE/DDN
       if (reporting_location.getString().length() == 0
            || name.getString().length() == 0
            || death_location.getString().length() == 0
            || living_children.getString().length() == 0
            || dead_children.getString().length() == 0) {
            return false;
        }
        return true;
    }

    /*
     * Whether all filled data is correct
     * @return <code>true</code> if all fields are OK
     * <code>false</code> otherwise.
     */

    public boolean isValid() {
        ErrorMessage = "La date indiquée est dans le futur.";

        if (pregnantField.getString(pregnantField.getSelectedIndex()).equals("OUI") &&
            pregnancy_weeks.getString().length() == 0) {
            ErrorMessage = "La durée de la grossesse est obligatoire si la femme est enceinte.";
            return false;
        }
        if (pregnantField.getString(pregnantField.getSelectedIndex()).equals("NON") &&
            pregnancy_weeks.getString().length() != 0) {
            ErrorMessage = "La femme doit être enceinte s'il y a une durée de grossesse";
            return false;
        }

        if (SharedChecks.isDateValide(reporting_date.getDate()) != true) {
            ErrorMessage = "(Date repportage) " + ErrorMessage;
            return false;
        }
        if (SharedChecks.isDateValide(dob.getDate()) != true) {
            ErrorMessage = "(Date de naissance) " + ErrorMessage;
            return false;
        }
        if (SharedChecks.isDateValide(dod.getDate())!= true){
            ErrorMessage = "(Date de la mort) " + ErrorMessage;
            return false;
        }

        if (SharedChecks.compareDobDod(dob.getDate(), dod.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de la mort est superieur à la date de la naissance";
            return false;
        }

        if (age.getString().length()!= 0){
            String age_nbr = String.valueOf(age.getString().charAt(age.getString().length() - 1));

            if (!age_nbr.equals("a") && !age_nbr.equals("m")){
                ErrorMessage = "Le nombre d'age doit être suivi d'un 'a' pour l'année ou d'un 'm' pour le mois";
                return false;
            }
        }
        
        if (SharedChecks.ValidateCode(reporting_location.getString()) == true) {
            ErrorMessage = "[Code village (visite)] ce code n'est pas valide";
            return false;
        }
        return true;
    }

   /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */

    public String toSMSFormat() {
        
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

        int pregnancy_related;
        if ((pregnancy_related_deathField.getString(pregnancy_related_deathField.getSelectedIndex()).equals("N/A"))){
            pregnancy_related = -1;
        } else {
            pregnancy_related = pregnancy_related_deathField.getSelectedIndex();
        }

        String pregnancy_w = "-1";
        if (pregnantField.getString(pregnantField.getSelectedIndex()).equals("OUI")){
            pregnancy_w = pregnancy_weeks.getString();
        }

        // fnuap dpw reporting_location name dob dod death_location
        // living_children dead_children pregnant pregnancy_weeks
        // pregnancy_related_death
        return "fnuap dpw" + sep + reporting_d
                           + sep + reporting_location.getString() 
                           + sep + name.getString().replace(' ', '_')
                           + sep + fdob
                           + sep + dod_d
                           + sep + death_location.getString()
                           + sep + living_children.getString() 
                           + sep + dead_children.getString()
                           + sep + pregnantField.getSelectedIndex() 
                           + sep + pregnancy_w
                           + sep + pregnancy_related;
    }

    public String toText() {
        return "F]: " + name.getString();
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "maternal");
                                      this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command
        if (c == CMD_SEND) {

            Alert alert;

            // check whether all fields have been completed
                // if not, we alert and don't do anything else.
            if (!this.isComplete()) {
                alert = new Alert("Données manquantes", "Tous les champs " +
                                  "requis doivent être remplis!", null,
                                   AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // check for errors and display first error
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
