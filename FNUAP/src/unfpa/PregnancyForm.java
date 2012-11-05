
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import java.util.Date;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.HelpForm.*;
import unfpa.SharedChecks.*;
import unfpa.Entities.*;

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

    private String ErrorMessage = "";
    private static final String[] choix = {Constants.NON, Constants.OUI};
    private static final String[] pregnancy_result = {"Né vivant", "Mort-né", "Avortement"};

    //private TextField reporting_locationField;
    private TextField householder_name;
    private DateField reporting_date;
    private TextField mother_name;
    private ChoiceGroup reporting_locationField;
    private TextField age;
    private ChoiceGroup end_pregnancyfield;
    private TextField pregnancy_age;
    private DateField expected_delivery_date;
    private ChoiceGroup pregnancy_resultfield;
    private DateField delivery_date;

    String sep = " ";


    public PregnancyForm(UNFPAMIDlet midlet) {
        super("Données de grossesses");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        String commune_code = config.get("commune_code");
        String old_ind_reporting = config.get("reporting_location");

        householder_name = new TextField("Nom du chef de ménage:", null, 20, TextField.ANY);
        reporting_date = new DateField("Date de visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(new Date());

        reporting_locationField = new ChoiceGroup("Code village (visite):", ChoiceGroup.POPUP, Entities.villages_names(commune_code), null);
        reporting_locationField.setSelectedIndex(Integer.parseInt(old_ind_reporting), true);

        mother_name = new TextField("Nom de la mère:", null, 20, TextField.ANY);
        age = new TextField("Age:", null, 4, TextField.NUMERIC);
        pregnancy_age = new TextField("Age de la grossesse (en mois):",  null, 2, TextField.NUMERIC);
        expected_delivery_date =  new DateField("Date probable d'accouchement:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        expected_delivery_date.setDate(new Date());
        end_pregnancyfield = new ChoiceGroup("La grossesse est-elle terminée?", ChoiceGroup.POPUP, choix, null);
        pregnancy_resultfield = new ChoiceGroup("Issue de la grossesse:", ChoiceGroup.POPUP, pregnancy_result, null);
        delivery_date = new DateField("Date de l'issue de la grossesse:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        delivery_date.setDate(new Date());

        // add fields to forms
        append(reporting_date);
        append(reporting_locationField);
        append(householder_name);
        append(mother_name);
        append(age);
        append(pregnancy_age);
        append(expected_delivery_date);
        append(end_pregnancyfield);
        append(pregnancy_resultfield);
        append(delivery_date);

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
       if (householder_name.getString().length() == 0
           || mother_name.getString().length() == 0
           || pregnancy_age.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        int agepregnancy =  Integer.parseInt(pregnancy_age.getString());
        ErrorMessage = "La date indiquée est dans le futur.";


        if (SharedChecks.isDateValide(reporting_date.getDate()) != true) {
            ErrorMessage = "[Date de visite] " + ErrorMessage;
            return false;
        }

        // if (SharedChecks.isDateValide(expected_delivery_date.getDate()) == true) {
        //     ErrorMessage = "[Date probable d'accouchement] La date indiquée pas est dans le futur.";
        //     return false;
        // }

        if (SharedChecks.isDateValide(delivery_date.getDate()) != true) {
            ErrorMessage = "[Date de l'issue de la grossesse] " + ErrorMessage;
            return false;
        }

        if (SharedChecks.compareDobDod(delivery_date.getDate(), reporting_date.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de visite ne peut pas être inferieure à la date de l'issue de la grossesse";
            return false;
        }

        if (agepregnancy < 0){
            ErrorMessage = "[Age de la grossesse (en mois)] le nombre de mois doit être supérieur à zéro.";
            return false;
        }

        if (agepregnancy > 12){
                ErrorMessage = "[Age de la grossesse (en mois)] le nombre de mois doit être inférieur à 12.";
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

        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        String d_recording = String.valueOf(reporting_date_array[2])
                             + SharedChecks.addzero(reporting_date_array[1])
                             + SharedChecks.addzero(reporting_date_array[0]);

        int  expected_date_c_array[] = SharedChecks.formatDateString(expected_delivery_date.getDate());
            expect_date_c = String.valueOf(expected_date_c_array[2])
                            + SharedChecks.addzero(expected_date_c_array[1])
                            + SharedChecks.addzero(expected_date_c_array[0]);

        if (end_pregnancyfield.getString(end_pregnancyfield.getSelectedIndex()).equals(Constants.OUI)) {
            resul_pregnancy = pregnancy_resultfield.getSelectedIndex() + 1;

            int delivery_date_array[] = SharedChecks.formatDateString(delivery_date.getDate());
            d_pregnancy = String.valueOf(delivery_date_array[2])
                          + SharedChecks.addzero(delivery_date_array[1])
                          + SharedChecks.addzero(delivery_date_array[0]);
        }

        String prof = SharedChecks.profile();
        String commune_code = config.get("commune_code");

        String reporting_location_index = String.valueOf(reporting_locationField.getSelectedIndex());

        // On sauvegarde l'index pour l'ulitiser par defaut après        
        config.set("reporting_location", reporting_location_index);

        return "fnuap gpw" + sep + prof
                           + sep + Entities.villages_codes(commune_code)[reporting_locationField.getSelectedIndex()]
                           + sep + householder_name.getString().replace(' ', '_')
                           + sep + d_recording // Date
                           + sep + mother_name.getString().replace(' ', '_')
                           + sep + age.getString() + "a"
                           + sep + pregnancy_age.getString()
                           + sep + expect_date_c // Si la grossesse n'est pas terminé d_pregnancy = - si non une date(20120427)
                           + sep + resul_pregnancy  // Si la grossesse n'est pas terminer resul_pregnancy = -1 si non l'index de l'element chosi de {"Né vivant", "Mort-né", "Avortement"}
                           + sep + d_pregnancy; // Si la grossesse n'est pas terminé d_pregnancy = - si non une date(20120427)
        }

    public String toText() {
        int date_recording_array[] = SharedChecks.formatDateString(reporting_date.getDate());

        return "G-" +  date_recording_array[0] + "] " + householder_name.getString();
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
