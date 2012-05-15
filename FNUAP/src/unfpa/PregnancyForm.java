
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

public class PregnancyForm extends Form implements CommandListener {

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
    private static final String[] choix = {"NON", "OUI"};
    private static final String[] resulting_pregnancy = {"Né vivant", "Mort-né", "Avortement"};

    //private TextField reporting_location;
    private TextField name_household_head;
    private DateField date_recording;
    private TextField name_pregnant_woman;
    private TextField reporting_location;
    private TextField age;
    private ChoiceGroup end_pregnancyfield;
    private TextField age_pregnancy;
    private DateField expected_date_confinement;
    private ChoiceGroup resulting_pregnancyfield;
    private DateField date_pregnancy;


    public PregnancyForm(UNFPAMIDlet midlet) {
        super("Données de grossesses");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        name_household_head = new TextField("Nom du chef de menage:", null, 20, TextField.ANY);
        date_recording = new DateField("Date de visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        date_recording.setDate(new Date());
        reporting_location = new TextField("Code village (visite):", null, Constants.LOC_CODE_MAX, TextField.ANY);
        name_pregnant_woman = new TextField("Nom de la femme enceinte:", null, 20, TextField.ANY);
        age =  new TextField("Age:", null, 4, TextField.NUMERIC);
        age_pregnancy = new TextField("Age de la grossesse (en mois):",  null, 2, TextField.NUMERIC);
        expected_date_confinement =  new DateField("Date probable d'accouchement:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        expected_date_confinement.setDate(new Date());
        end_pregnancyfield = new ChoiceGroup("La grossesse est-elle terminée?", ChoiceGroup.POPUP, choix, null);
        resulting_pregnancyfield = new ChoiceGroup("Issue de la grossesse:", ChoiceGroup.POPUP, resulting_pregnancy, null);
        date_pregnancy = new DateField("Date de l'issue de la grossesse:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        date_pregnancy.setDate(new Date());

        // add fields to forms
        append(date_recording);
        append(reporting_location);
        append(name_household_head);
        append(name_pregnant_woman);
        append(age);
        append(age_pregnancy);
        append(expected_date_confinement);
        append(end_pregnancyfield);
        append(resulting_pregnancyfield);
        append(date_pregnancy);

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
       if (reporting_location.getString().length() == 0
           || name_household_head.getString().length() == 0
           || name_pregnant_woman.getString().length() == 0
           || age_pregnancy.getString().length() == 0) {
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
        int agepregnancy =  Integer.parseInt(age_pregnancy.getString());

        if (agepregnancy < 0){
            ErrorMessage = "Le nombre de mois doit être supérieur à zéro.";
            return false;
        }
        if (agepregnancy > 12){
                ErrorMessage = "Le nombre de mois doit être inférieur à 12.";
                return false;
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

        String expect_date_c = "-";
        int resul_pregnancy = 0;
        String d_pregnancy = "-";
        
        int date_recording_array[] = SharedChecks.formatDateString(date_recording.getDate());
        String d_recording = String.valueOf(date_recording_array[2]) 
                             + SharedChecks.addzero(date_recording_array[1])
                             + SharedChecks.addzero(date_recording_array[0]);

        int  expected_date_c_array[] = SharedChecks.formatDateString(expected_date_confinement.getDate());
            expect_date_c = String.valueOf(expected_date_c_array[2])
                            + SharedChecks.addzero(expected_date_c_array[1])
                            + SharedChecks.addzero(expected_date_c_array[0]);

        if (end_pregnancyfield.getString(end_pregnancyfield.getSelectedIndex()).equals("OUI")) {
            resul_pregnancy = resulting_pregnancyfield.getSelectedIndex() + 1;

            int date_pregnancy_array[] = SharedChecks.formatDateString(date_pregnancy.getDate());
            d_pregnancy = String.valueOf(date_pregnancy_array[2])
                          + SharedChecks.addzero(date_pregnancy_array[1])
                          + SharedChecks.addzero(date_pregnancy_array[0]);
        }

        return "fnuap gpw" + sep + reporting_location.getString()
                           + sep + name_household_head.getString().replace(' ', '_')
                           + sep + d_recording // Date
                           + sep + name_pregnant_woman.getString().replace(' ', '_')
                           + sep + age.getString()
                           + sep + age_pregnancy.getString()
                           + sep + expect_date_c // Si la grossesse n'est pas terminer d_pregnancy = - si non une date(20120427)
                           + sep + resul_pregnancy  // Si la grossesse n'est pas terminer resul_pregnancy = -1 si non l'index de l'element chosi de {"Né vivant", "Mort-né", "Avortement"}
                           + sep + d_pregnancy; // Si la grossesse n'est pas terminer d_pregnancy = - si non une date(20120427)
        }

    public String toText() {
        return "F] " + name_household_head.getString() ;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "pregnancy");
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
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            }
        }
    }
}
