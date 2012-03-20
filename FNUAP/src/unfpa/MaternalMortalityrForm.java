
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import java.util.Date;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.HelpForm.*;

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
    private static final int MAX_SIZE = 5; // max no. of chars per field.

    public UNFPAMIDlet midlet;

    private Configuration config;

    private String ErrorMessage = "";
    private static final String[] pergnancy_status_of_woman = {"OUI", "NON"};
    private static final String[] pregnancy_related_deaths = {"OUI", "NON", "Non applicable"};

    //General Informatien
    private DateField reporting_date;
    private TextField reporting_location;

    // Maternal Mortality Form
    private TextField name_of_deceased;
    private DateField age_of_deceased;
    private DateField date_of_death;
    private TextField place_of_death;
    private TextField living_children_of_deceased;
    private TextField dead_children_of_deceased;
    private ChoiceGroup pergnancy_status_of_womanField;
    private TextField duration_of_pregnancy;
    private ChoiceGroup pregnancy_related_deathsField;


public MaternalMortalityrForm(UNFPAMIDlet midlet) {
    super("Mortalité maternelle");
    this.midlet = midlet;

    config = new Configuration();

    // creating all fields (blank);

    reporting_date =  new DateField("Date de rapport:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    reporting_location =  new TextField("Lieu de rapport:", null, 20, TextField.ANY);
    
    name_of_deceased =  new TextField("le nom du défunt:", null, 20, TextField.ANY);
    age_of_deceased =  new DateField("l'âge de la personne décédée:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    date_of_death =  new DateField("Date de la mort:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    place_of_death =  new TextField("le lieu du décès:", null, 20, TextField.ANY);
    living_children_of_deceased =  new TextField("enfants vivant du défunt:", null, 20, TextField.ANY);
    dead_children_of_deceased =  new TextField("enfants morts de la personne décédée:", null, 20, TextField.ANY);
    pergnancy_status_of_womanField = new ChoiceGroup("pergnancy_status_of_womanField:", ChoiceGroup.POPUP, pergnancy_status_of_woman, null);
    duration_of_pregnancy =  new TextField("durée de la grossesse:", null, 20, TextField.ANY);
    pregnancy_related_deathsField = new ChoiceGroup("décès liés à la grossesse:", ChoiceGroup.POPUP, pregnancy_related_deaths, null);

    reporting_date.setDate(new Date());
    age_of_deceased.setDate(new Date());
    date_of_death.setDate(new Date());

    // add fields to forms
    append(reporting_date);
    append(reporting_location);
    append(name_of_deceased);
    append(age_of_deceased);
    append(date_of_death);
    append(place_of_death);
    append(living_children_of_deceased);
    append(dead_children_of_deceased);
    append(pergnancy_status_of_womanField);
    append(duration_of_pregnancy);
    append(pregnancy_related_deathsField);

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
            name_of_deceased.getString().length() == 0 ||
            place_of_death.getString().length() == 0 ||
            living_children_of_deceased.getString().length() == 0 ||
            dead_children_of_deceased.getString().length() == 0 ||
            duration_of_pregnancy.getString().length() == 0 ||
            dead_children_of_deceased.getString().length() == 0 ||
            dead_children_of_deceased.getString().length() == 0 ) {
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
        return true;
    }

   /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */

    public String toSMSFormat() {
        String sep = " ";
        return "unfpa malmor" + sep + place_of_death.getString();
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
                            " la demande par SMS.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
