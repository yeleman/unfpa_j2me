
package unfpa;

import javax.microedition.lcdui.*;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.HelpForm.*;

/**
 * J2ME Form allowing Server number, health center and hc_code editing.
 * Saves the new number into <code>Configuration</code>
 * Saves the new health center into <code>Configuration</code>
 * Saves the new hc_code into <code>Configuration</code>
 * @author fadiga
 */
public class OptionForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Enreg.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    
    private Configuration config;
    private TextField numberField;
    private TextField cscom_code;
    private TextField profile;
    UNFPAMIDlet midlet;

public OptionForm(UNFPAMIDlet midlet) {
    super("Paramètres de transmission");
    this.midlet = midlet;
    
    config = new Configuration();

    // retrieve phone number from config
    // if not present, use constant
    String phone_number = "";
    phone_number = config.get("server_number");
    if (phone_number.equals("")) {
        phone_number = Constants.server_number;
    }

    numberField = new TextField ("Numéro du serveur:", phone_number, 8, TextField.PHONENUMBER);
    cscom_code = new TextField("Code CSCOM", config.get("cscom_code"), 20, TextField.ANY);
    profile = new TextField("Code CSCOM", config.get("profile"), 20, TextField.ANY);

    append(numberField);
    append(cscom_code);
    append(profile);

    addCommand(CMD_EXIT);
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
        if (numberField.getString().length() == 0) {
            return false;
        }
        if (cscom_code.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public void commandAction(Command c, Displayable d) {
        // Help command displays Help Form
         if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "option");
            this.midlet.display.setCurrent(h);
        }

        // exit command goes back to Main Menu
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command stores new number in config or display errors.
        if (c == CMD_SAVE) {

            Alert alert;
            // check whether all fields have been completed
            // if not, we alert and don't do anything else.
            if (!this.isComplete()) {
                alert = new Alert("Données manquantes", "Tous les champs doivent être remplis!", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }
            if (config.set("server_number", numberField.getString()) && config.set("cscom_code", cscom_code.getString()) && config.set("profile", profile.getString())) {
                alert = new Alert ("Confirmation!", "Votre modification a été bien enregistré.", null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec", "Impossible d'enregistrer cette modification.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
