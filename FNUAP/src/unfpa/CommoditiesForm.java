
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
public class CommoditiesForm extends Form implements CommandListener {

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
    private static final String[] choice = {"OUI", "NON"};

    //General Informatien
    private TextField reporting_year;
    private TextField reporting_month;
    private TextField location_of_sdp;
    private TextField family_planning;
    private TextField delivery_services;
    private ChoiceGroup male_condom;
    private ChoiceGroup female_condom;
    private ChoiceGroup oral_pills;
    private ChoiceGroup injectable;
    private ChoiceGroup iud;
    private ChoiceGroup implants;
    private ChoiceGroup female_sterilization;
    private ChoiceGroup male_sterilization;
    private ChoiceGroup amoxicillin_ij;
    private ChoiceGroup amoxicillin_cap_gel;
    private ChoiceGroup amoxicillin_suspension;
    private ChoiceGroup azithromycine_tab;
    private ChoiceGroup azithromycine_suspension;
    private ChoiceGroup benzathine_penicillin;
    private ChoiceGroup cefexime;
    private ChoiceGroup clotrimazole;
    private ChoiceGroup ergometrine_tab;
    private ChoiceGroup ergometrine_vials;
    private ChoiceGroup iron;
    private ChoiceGroup folate;
    private ChoiceGroup iron_folate;
    private ChoiceGroup magnesium_sulfate;
    private ChoiceGroup metronidazole;
    private ChoiceGroup oxytocine;


public CommoditiesForm(UNFPAMIDlet midlet) {
    super("RH Commodities From");
    this.midlet = midlet;

    config = new Configuration();

    // creating all fields (blank);
    reporting_year = new TextField("l'année de déclaration", null, 20, TextField.ANY);
    reporting_month = new TextField("mois considéré", null, 20, TextField.ANY);
    location_of_sdp = new TextField("emplacement de sdp", null, 20, TextField.ANY);
    family_planning = new TextField("planification familiale", null, 20, TextField.ANY);
    delivery_services = new TextField("services de livraison", null, 20, TextField.ANY);

    male_condom = new ChoiceGroup("installation fournir préservatif masculin", ChoiceGroup.POPUP, choice, null);
    female_condom = new ChoiceGroup("installation fournir préservatif féminin", ChoiceGroup.POPUP, choice, null);
    oral_pills = new ChoiceGroup("installation de fournir la pilule", ChoiceGroup.POPUP, choice, null);
    injectable = new ChoiceGroup("installation fournir injectable", ChoiceGroup.POPUP, choice, null);
    iud = new ChoiceGroup("installation fournir DIU", ChoiceGroup.POPUP, choice, null);
    implants = new ChoiceGroup("installation fournir des implants", ChoiceGroup.POPUP, choice, null);
    female_sterilization = new ChoiceGroup("installation de fournir la stérilisation féminine", ChoiceGroup.POPUP, choice, null);
    male_sterilization = new ChoiceGroup("installation de fournir la stérilisation masculine", ChoiceGroup.POPUP, choice, null);
    amoxicillin_ij = new ChoiceGroup("amoxicilline injectable disponible", ChoiceGroup.POPUP, choice, null);
    amoxicillin_cap_gel = new ChoiceGroup("amoxicilline capsule disponibles", ChoiceGroup.POPUP, choice, null);
    amoxicillin_suspension = new ChoiceGroup("amoxicilline suspension disponibles", ChoiceGroup.POPUP, choice, null);
    azithromycine_tab = new ChoiceGroup("L'azithromycine (comprimé / gel).", ChoiceGroup.POPUP, choice, null);
    azithromycine_suspension = new ChoiceGroup("suspension d'azithromycine", ChoiceGroup.POPUP, choice, null);
    benzathine_penicillin = new ChoiceGroup("benzathine pénicilline", ChoiceGroup.POPUP, choice, null);
    cefexime = new ChoiceGroup("céfixime disponible", ChoiceGroup.POPUP, choice, null);
    clotrimazole = new ChoiceGroup("clotrimazole disponible", ChoiceGroup.POPUP, choice, null);
    ergometrine_tab = new ChoiceGroup("ergométrine (comprimé) disponible", ChoiceGroup.POPUP, choice, null);
    ergometrine_vials = new ChoiceGroup("ergométrine (flacons) disponible", ChoiceGroup.POPUP, choice, null);
    iron = new ChoiceGroup("fer disponible", ChoiceGroup.POPUP, choice, null);
    folate = new ChoiceGroup("acide folique disponibles", ChoiceGroup.POPUP, choice, null);
    iron_folate = new ChoiceGroup("fer / acide folique disponibles", ChoiceGroup.POPUP, choice, null);
    magnesium_sulfate = new ChoiceGroup("sulfate de magnésium disponible", ChoiceGroup.POPUP, choice, null);
    metronidazole = new ChoiceGroup("métronidazole disponibles", ChoiceGroup.POPUP, choice, null);
    oxytocine = new ChoiceGroup("l'ocytocine disponible", ChoiceGroup.POPUP, choice, null);

    append(reporting_year);
    append(reporting_month);
    append(location_of_sdp);
    append(family_planning);
    append(delivery_services);

    append(male_condom);
    append(female_condom);
    append(oral_pills);
    append(injectable);
    append(iud);
    append(implants);
    append(female_sterilization);
    append(male_sterilization);
    append(amoxicillin_ij);
    append(amoxicillin_cap_gel);
    append(amoxicillin_suspension);
    append(azithromycine_tab);
    append(azithromycine_suspension);
    append(benzathine_penicillin);
    append(cefexime);
    append(clotrimazole);
    append(ergometrine_tab);
    append(ergometrine_vials);
    append(iron);
    append(folate);
    append(iron_folate);
    append(magnesium_sulfate);
    append(metronidazole);
    append(oxytocine);
    
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
                implants.getSelectedIndex();
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
