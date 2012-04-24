
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
    private TextField age;
    private ChoiceGroup end_pregnancyfield;
    private TextField name_husband;
    private TextField age_pregnancy;
    private DateField expected_date_confinement;
    private ChoiceGroup resulting_pregnancyfield;
    private DateField date_pregnancy;


public PregnancyForm(UNFPAMIDlet midlet) {
    super("pregnancy");
    this.midlet = midlet;

    config = new Configuration();
    store = new SMSStore();

    name_household_head =  new TextField("Nom du chef de menage:", null, 20, TextField.ANY);
    date_recording =  new DateField("Date enregistrement:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    date_recording.setDate(new Date());
    name_pregnant_woman =  new TextField("Nom femme enceinte:", null, 20, TextField.ANY);
    age =  new TextField("Age:", null, 4, TextField.NUMERIC);
    name_husband =  new TextField("Nom du mari:", null, 20, TextField.ANY);
    age_pregnancy =  new TextField("Age grossesse en mois:",  null, 2, TextField.NUMERIC);
    expected_date_confinement =  new DateField("date probable d'accouchement:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    expected_date_confinement.setDate(new Date());
    end_pregnancyfield = new ChoiceGroup("La grossesse est terminé? ", ChoiceGroup.POPUP, choix, null);
    resulting_pregnancyfield = new ChoiceGroup("Issu de la grossesse:", ChoiceGroup.POPUP, resulting_pregnancy, null);
    date_pregnancy =  new DateField("date de issu de la grossesse:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    date_pregnancy.setDate(new Date());

    // add fields to forms
    append(name_household_head);
    append(date_recording);
    append(name_pregnant_woman);
    append(age);
    append(name_husband);
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
       if (name_household_head.getString().length() == 0 ||
            name_pregnant_woman.getString().length() == 0 ||
            name_husband.getString().length() == 0 ) {
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
            ErrorMessage = "le mois doit être supérieur a zéro.";
            return false;
        }
        if (agepregnancy > 12){
                ErrorMessage = "le mois doit être inférieur a 12.";
                return false;
            }

        return true;
    }

   /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */

    int date_recording_array[] = SharedChecks.formatDateString(date_recording.getDate());
    String d_recording = String.valueOf(date_recording_array[2]) + SharedChecks.addzero(date_recording_array[1]) + SharedChecks.addzero(date_recording_array[0]);

    int  expected_date_c_array[] = SharedChecks.formatDateString(expected_date_confinement.getDate());
    String expect_date_c = String.valueOf(expected_date_c_array[2]) + SharedChecks.addzero(expected_date_c_array[1]) + SharedChecks.addzero(expected_date_c_array[0]);
    
    public String toSMSFormat() {
        
        int resul_pregnancy = -1;
        String d_pregnancy = "-";

        if (end_pregnancyfield.getString(end_pregnancyfield.getSelectedIndex()).equals("OUI")){
            resul_pregnancy = resulting_pregnancyfield.getSelectedIndex();
            int date_pregnancy_array[] = SharedChecks.formatDateString(date_pregnancy.getDate());
            d_pregnancy = String.valueOf(date_pregnancy_array[2]) + SharedChecks.addzero(date_pregnancy_array[1]) + String.valueOf(date_pregnancy_array[0]);
        }

        return "fnuap gpw" + sep + name_household_head.getString()
                           + sep + d_recording
                           + sep + name_pregnant_woman.getString()
                           + sep + age.getString()
                           + sep + name_husband.getString()
                           + sep + age_pregnancy.getString()
                           + sep + expect_date_c
                           + sep + resul_pregnancy
                           + sep + d_pregnancy;
        }

    public String toText() {

        return "F]: " + name_household_head.getString() + sep + date_recording_array[0] +
                "/" + date_recording_array[1] + "/" + date_recording_array[2];
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
