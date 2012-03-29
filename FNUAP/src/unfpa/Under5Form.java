
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.SharedChecks.*;


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

    private StringItem helpText;
    UNFPAMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;

    //register
    private DateField reporting_date;
    private ChoiceGroup reporting_location;
    private ChoiceGroup death_location;
    private TextField name;
    private DateField dob;
    private TextField dob1;
    private DateField dod;
    java.util.Date now = new java.util.Date();
    private static final String[] location= {"kati", "kkro"};

    public Under5Form(UNFPAMIDlet midlet) {
        super("Mortalité moin de 5ans");
        this.midlet = midlet;
    
        config = new Configuration();

        reporting_date =  new DateField("Date de rapportage:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(now);
        reporting_location = new ChoiceGroup("Code village:", ChoiceGroup.POPUP, location, null);
        name = new TextField("Nom et prenom", null, 20, TextField.ANY);
        dob =  new DateField("Date de naussnce:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dob.setDate(now);
        dob1 =  new TextField("Ou son âge:", null, 20, TextField.ANY);
        dod =  new DateField("Date de la mort:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dod.setDate(now);
        death_location = new ChoiceGroup("Code village:", ChoiceGroup.POPUP, location, null);
        append(reporting_date);
        append(reporting_location);
        append(name);
        append(dob);
        append(dob1);
        append(dod);
        append(death_location);
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
    
    public String toSMSFormat() {
        return "moins de 5ans" + dob.getDate();
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
                alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                            " la demande par SMS.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }

        }
    }
}
