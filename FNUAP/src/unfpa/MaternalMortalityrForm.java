
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
    private static final Command CMD_SAVE = new Command ("Enregistrer.",
                                                            Command.OK, 2);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 3);
    public UNFPAMIDlet midlet;

    private Configuration config;

    private String ErrorMessage = "";
    private static final String[] pregnant = {"OUI", "NON"};
    private static final String[] pregnancy_related_death = {"OUI", "NON", "Non applicable"};
    private static final String month_list[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    //General Informatien
    private DateField reporting_date;
    private TextField reporting_location;

    // Maternal Mortality Form
    private TextField name;
    private DateField dob;
    private TextField dob1;
    private DateField dod;
    private TextField place_of_death;
    private TextField living_children;
    private TextField dead_children;
    private ChoiceGroup pregnantField;
    private TextField pregnancy_weeks;
    private ChoiceGroup pregnancy_related_deathField;


public MaternalMortalityrForm(UNFPAMIDlet midlet) {
    super("Mortalité maternelle");
    this.midlet = midlet;

    config = new Configuration();

    // creating all fields (blank);

    reporting_date =  new DateField("Date de rapport:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    reporting_location =  new TextField("Lieu de rapport:", null, 20, TextField.ANY);
    
    name =  new TextField("Le nom du défunt:", null, 20, TextField.ANY);
    dob =  new DateField("Date de naissance de la personne décédée:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    dob1 =  new TextField("Ou son âge(Année):", null, 20, TextField.DECIMAL);
    dod =  new DateField("Date de la mort:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    place_of_death =  new TextField("Le lieu du décès:", null, 20, TextField.ANY);
    living_children =  new TextField("Enfants vivant du défunt:", null, 20, TextField.DECIMAL);
    dead_children =  new TextField("Enfants morts de la personne décédée:", null, 20, TextField.DECIMAL);
    pregnantField = new ChoiceGroup("Enceinte:", ChoiceGroup.POPUP, pregnant, null);
    pregnancy_weeks =  new TextField("Durée de la grossesse:", null, 20, TextField.ANY);
    pregnancy_related_deathField = new ChoiceGroup("Décès liés à la grossesse:", ChoiceGroup.POPUP, pregnancy_related_death, null);

    reporting_date.setDate(new Date());
    dob.setDate(new Date());
    dod.setDate(new Date());

    // add fields to forms
    append(reporting_date);
    append(reporting_location);
    append(name);
    append(dob);
    append(dob1);
    append(dod);
    append(place_of_death);
    append(living_children);
    append(dead_children);
    append(pregnantField);
    append(pregnancy_weeks);
    append(pregnancy_related_deathField);

    addCommand(CMD_EXIT);
    addCommand(CMD_SEND);
    addCommand(CMD_SAVE);
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
       if (reporting_location.getString().length() == 0 ||
            name.getString().length() == 0 ||
            place_of_death.getString().length() == 0 ||
            living_children.getString().length() == 0 ||
            dead_children.getString().length() == 0 ||
            pregnancy_weeks.getString().length() == 0) {
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
        return true;
    }

   /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */

    public String toSMSFormat() {
        String sep = " ";
        
        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        int day = dob_array[0];
        int month = dob_array[1];
        int year = dob_array[2];

        return "unfpa malmor" + sep + place_of_death.getString() 
                              + sep + year + "-" + month + "-" + day;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "registration");
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
                alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                                   " la demande par SMS.", null,
                                   AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
