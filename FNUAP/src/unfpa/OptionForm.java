
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
    private ChoiceGroup profileField;
    private TextField cscom_code;
    private ChoiceGroup district;
    private String[] districts;

    private static final String[] profile = {"CREDOS", "FNUAP"};
    UNFPAMIDlet midlet;

public OptionForm(UNFPAMIDlet midlet) {
    super("Paramètres de transmission");
    this.midlet = midlet;
    
    config = new Configuration();
    districts = Constants.codes_district();

    // retrieve phone number from config
    // if not present, use constant
    String phone_number = "";
    phone_number = config.get("server_number");
    if (phone_number.equals("")) {
        phone_number = Constants.server_number;
    }

    numberField = new TextField ("Numéro du serveur:", phone_number, 8, TextField.PHONENUMBER);
    cscom_code = new TextField("Code CSCOM", config.get("cscom_code"), 20, TextField.ANY);
    district = new ChoiceGroup("District", ChoiceGroup.POPUP, Constants.names_district(), null);
    profileField = new ChoiceGroup("Profile", ChoiceGroup.POPUP, profile, null);

    int sel = 0;
    String my_profile = config.get("profile");
    for (int i = 0; i<profile.length ; i++) {
        if (profile[i].equals(my_profile)) {
            sel = i;
            break;
        }
    }
    int dis_index = 0;
    String my_dis = config.get("district_code");
    for (int i=0; i<districts.length;i++) {
        if (districts[i].equals(my_dis)) {
            dis_index = i;
            break;
        }
    }

    profileField.setSelectedIndex(sel, true);
    district.setSelectedIndex(dis_index, true);

    append(numberField);
    append(cscom_code);
    append(profileField);
    append(district);

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

            String code_district = districts[district.getSelectedIndex()];
            // System.out.println("Code SELECTION: " + code_district);

            if (config.set("server_number", numberField.getString()) && 
                    config.set("cscom_code", cscom_code.getString()) &&
                    config.set("district_code", code_district) &&
                    config.set("profile", profileField.getString(profileField.getSelectedIndex()))) {
                alert = new Alert ("Confirmation!", "Votre modification a été bien enregistré.", null, AlertType.CONFIRMATION);
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec", "Impossible d'enregistrer cette modification.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
