
package unfpa;

import javax.microedition.lcdui.*;
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
public class StockProduitForm extends Form implements CommandListener {

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
    private static final String[] choice = {"OUI", "NON"};

    //General Informatien
    private TextField reporting_year;
    private TextField reporting_month;
    private TextField location_of_sdp;
    private TextField family_planning;
    private TextField delivery_services;
    private ChoiceGroup does_facility_provide_male_condom_Field;
    private ChoiceGroup does_facility_provide_female_condom_Field;
    private ChoiceGroup does_facility_provide_oral_pills_Field;
    private ChoiceGroup does_facility_provide_injectable_Field;
    private ChoiceGroup does_facility_provide_iud_Field;
    private ChoiceGroup does_facility_provide_implants_Field;
    private ChoiceGroup does_facility_provide_female_sterilization_Field;
    private ChoiceGroup does_facility_provide_male_sterilization_Field;
    private ChoiceGroup is_amoxicillin_available_injectable_Field;
    private ChoiceGroup is_amoxicillin_available_capsule_Field;
    private ChoiceGroup is_amoxicillin_available_suspension_Field;
    private ChoiceGroup is_amoxicillin_available_table_Field;
    private ChoiceGroup is_benzatthine_penicilable_Field;
    private ChoiceGroup is_cefexime_available_Field;
    private ChoiceGroup is_clotrimazole_available_Field;
    private ChoiceGroup is_ergometrine_available_Field;
    private ChoiceGroup is_iron_available_Field;
    private ChoiceGroup is_folate_available_Field;
    private ChoiceGroup is_iron_folate_available_Field;
    private ChoiceGroup is_magnesium_sulfate_available_Field;
    private ChoiceGroup is_metronidazole_available_Field;
    private ChoiceGroup is_oxytocine_available_Field;


public StockProduitForm(UNFPAMIDlet midlet) {
    super("Mortalité maternelle");
    this.midlet = midlet;

    config = new Configuration();

    // creating all fields (blank);
    reporting_year = new TextField("l'année de déclaration", null, 20, TextField.ANY);
    reporting_month = new TextField("mois considéré", null, 20, TextField.ANY);
    location_of_sdp = new TextField("emplacement de sdp", null, 20, TextField.ANY);
    family_planning = new TextField("planification familiale", null, 20, TextField.ANY);
    delivery_services = new TextField("services de livraison", null, 20, TextField.ANY);

    does_facility_provide_male_condom_Field = new ChoiceGroup("installation fournir préservatif masculin", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_female_condom_Field = new ChoiceGroup("installation fournir préservatif féminin", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_oral_pills_Field = new ChoiceGroup("installation de fournir la pilule", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_injectable_Field = new ChoiceGroup("installation fournir injectable", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_iud_Field = new ChoiceGroup("installation fournir DIU", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_implants_Field = new ChoiceGroup("installation fournir des implants", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_female_sterilization_Field = new ChoiceGroup("installation de fournir la stérilisation féminine", ChoiceGroup.POPUP, choice, null);
    does_facility_provide_male_sterilization_Field = new ChoiceGroup("installation de fournir la stérilisation masculine", ChoiceGroup.POPUP, choice, null);
    is_amoxicillin_available_injectable_Field = new ChoiceGroup("amoxicilline injectable disponible", ChoiceGroup.POPUP, choice, null);
    is_amoxicillin_available_capsule_Field = new ChoiceGroup("amoxicilline capsule disponibles", ChoiceGroup.POPUP, choice, null);
    is_amoxicillin_available_suspension_Field = new ChoiceGroup("amoxicilline suspension disponibles", ChoiceGroup.POPUP, choice, null);
    is_amoxicillin_available_table_Field = new ChoiceGroup("amoxicilline table disponible", ChoiceGroup.POPUP, choice, null);
    is_benzatthine_penicilable_Field = new ChoiceGroup("est la benzathine pénicilline", ChoiceGroup.POPUP, choice, null);
    is_cefexime_available_Field = new ChoiceGroup("céfixime disponible", ChoiceGroup.POPUP, choice, null);
    is_clotrimazole_available_Field = new ChoiceGroup("clotrimazole disponible", ChoiceGroup.POPUP, choice, null);
    is_ergometrine_available_Field = new ChoiceGroup("ergométrine disponible", ChoiceGroup.POPUP, choice, null);
    is_iron_available_Field = new ChoiceGroup("fer disponible", ChoiceGroup.POPUP, choice, null);
    is_folate_available_Field = new ChoiceGroup("acide folique disponibles", ChoiceGroup.POPUP, choice, null);
    is_iron_folate_available_Field = new ChoiceGroup("fer / acide folique disponibles", ChoiceGroup.POPUP, choice, null);
    is_magnesium_sulfate_available_Field = new ChoiceGroup("sulfate de magnésium disponible", ChoiceGroup.POPUP, choice, null);
    is_metronidazole_available_Field = new ChoiceGroup("métronidazole disponibles", ChoiceGroup.POPUP, choice, null);
    is_oxytocine_available_Field = new ChoiceGroup("l'ocytocine disponible", ChoiceGroup.POPUP, choice, null);

    append(reporting_year);
    append(reporting_month);
    append(location_of_sdp);
    append(family_planning);
    append(delivery_services);

    append(does_facility_provide_male_condom_Field);
    append(does_facility_provide_female_condom_Field);
    append(does_facility_provide_oral_pills_Field);
    append(does_facility_provide_injectable_Field);
    append(does_facility_provide_iud_Field);
    append(does_facility_provide_implants_Field);
    append(does_facility_provide_female_sterilization_Field);
    append(does_facility_provide_male_sterilization_Field);
    append(is_amoxicillin_available_injectable_Field);
    append(is_amoxicillin_available_capsule_Field);
    append(is_amoxicillin_available_suspension_Field);
    append(is_amoxicillin_available_table_Field);
    append(is_benzatthine_penicilable_Field);
    append(is_cefexime_available_Field);
    append(is_clotrimazole_available_Field);
    append(is_ergometrine_available_Field);
    append(is_iron_available_Field);
    append(is_folate_available_Field);
    append(is_iron_folate_available_Field);
    append(is_magnesium_sulfate_available_Field);
    append(is_metronidazole_available_Field);
    append(is_oxytocine_available_Field);
    
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
        if (reporting_year.getString().length() == 0  ||
            reporting_month.getString().length() == 0 ||
            location_of_sdp.getString().length() == 0 ||
            family_planning.getString().length() == 0 ||
            delivery_services.getString().length() == 0)
            {
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
        return "unfpa stock" + sep + 
                does_facility_provide_implants_Field.getSelectedIndex();
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
                             "requis doivent être remplis!", null, AlertType.ERROR);
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
