 
package unfpa;

import javax.microedition.lcdui.*;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.HelpForm.*;
import unfpa.SMSStore.*;
import unfpa.SharedChecks.*;

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
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 3);

    public UNFPAMIDlet midlet;

    private Configuration config;
    private SMSStore store;

    private String ErrorMessage = "";
    private static final String[] choice = {"Non", "Oui"};
    private static final String[] year_list = {"2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};
    private static final String[] yesnoavail = {"Non", "Oui (dispo.)", "Oui (non dispo.)"};
    private static final String month_list[] = {"----", "(01)Jan","(02)Feb","(03)Mar","(04)Apr","(05)May","(06)Jun","(07)Jul","(08)Aug","(09)Sep","(10)Oct","(11)Nov","(12)Dec"};

    //General Informatien
    private ChoiceGroup reporting_year;
    private ChoiceGroup reporting_month;
    private ChoiceGroup family_planning;
    private ChoiceGroup delivery_services;
    private ChoiceGroup male_condom;
    private TextField nb_male_condom;
    private ChoiceGroup female_condom;
    private TextField nb_female_condom;
    private ChoiceGroup oral_pills;
    private TextField nb_oral_pills;
    private ChoiceGroup injectable;
    private TextField nb_injectable;
    private ChoiceGroup iud;
    private TextField nb_iud;
    private ChoiceGroup implants;
    private TextField nb_implants;
    private ChoiceGroup female_sterilization;
    private ChoiceGroup male_sterilization;
    private ChoiceGroup amoxicillin_ij;
    private TextField nb_amoxicillin_ij;
    private ChoiceGroup amoxicillin_cap_gel;
    private TextField nb_amoxicillin_cap_gel;
    private ChoiceGroup amoxicillin_suspension;
    private TextField nb_amoxicillin_suspension;
    private ChoiceGroup azithromycine_tab;
    private TextField nb_azithromycine_tab;
    private ChoiceGroup azithromycine_suspension;
    private TextField nb_azithromycine_suspension;
    private ChoiceGroup benzathine_penicillin;
    private TextField nb_benzathine_penicillin;
    private ChoiceGroup cefexime;
    private TextField nb_cefexime;
    private ChoiceGroup clotrimazole;
    private TextField nb_clotrimazole;
    private ChoiceGroup ergometrine_tab;
    private TextField nb_ergometrine_tab;
    private ChoiceGroup ergometrine_vials;
    private TextField nb_ergometrine_vials;
    private ChoiceGroup iron;
    private TextField nb_iron;
    private ChoiceGroup folate;
    private TextField nb_folate;
    private ChoiceGroup iron_folate;
    private TextField nb_iron_folate;
    private ChoiceGroup magnesium_sulfate;
    private TextField nb_magnesium_sulfate;
    private ChoiceGroup metronidazole;
    private TextField nb_metronidazole;
    private ChoiceGroup oxytocine;
    private TextField nb_oxytocine;


public CommoditiesForm(UNFPAMIDlet midlet) {
    super("Dispo. Produits");
    this.midlet = midlet;

    config = new Configuration();
    store = new SMSStore();

    // creating all fields (blank);
    reporting_year = new ChoiceGroup("Année", ChoiceGroup.POPUP, year_list, null);
    reporting_month = new ChoiceGroup("Mois", ChoiceGroup.POPUP, month_list, null);

    family_planning = new ChoiceGroup("planning familial", ChoiceGroup.POPUP, choice, null);
    delivery_services = new ChoiceGroup("Accouchement", ChoiceGroup.POPUP, choice, null);

    male_condom = new ChoiceGroup("Préservatif masculin", ChoiceGroup.POPUP, choice, null);
    nb_male_condom = new TextField("Quantité dispo. (pièces)", null, 6, TextField.NUMERIC);
    female_condom = new ChoiceGroup("Préservatif féminin", ChoiceGroup.POPUP, choice, null);
    nb_female_condom = new TextField("Quantité dispo. (pièces)", null, 6, TextField.NUMERIC);
    oral_pills = new ChoiceGroup("Pilule", ChoiceGroup.POPUP, choice, null);
    nb_oral_pills = new TextField("Quantité dispo. (cycles)", null, 6, TextField.NUMERIC);
    injectable = new ChoiceGroup("Injectable", ChoiceGroup.POPUP, choice, null);
    nb_injectable = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);
    iud = new ChoiceGroup("DIU", ChoiceGroup.POPUP, choice, null);
    nb_iud = new TextField("Quantité dispo. (unité)", null, 6, TextField.NUMERIC);
    implants = new ChoiceGroup("Implants", ChoiceGroup.POPUP, choice, null);
    nb_implants = new TextField("Quantité dispo. (unité)", null, 6, TextField.NUMERIC);
    female_sterilization = new ChoiceGroup("Stérilisation féminine", ChoiceGroup.POPUP, yesnoavail, null);
    male_sterilization = new ChoiceGroup("Stérilisation masculine", ChoiceGroup.POPUP, yesnoavail, null);
    amoxicillin_ij = new ChoiceGroup("Amoxicilline injectable", ChoiceGroup.POPUP, choice, null);
    nb_amoxicillin_ij = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);
    amoxicillin_cap_gel = new ChoiceGroup("Amoxicilline capsule", ChoiceGroup.POPUP, choice, null);
    nb_amoxicillin_cap_gel = new TextField("Quantité dispo. (capsules)", null, 6, TextField.NUMERIC);
    amoxicillin_suspension = new ChoiceGroup("Amoxicilline suspension", ChoiceGroup.POPUP, choice, null);
    nb_amoxicillin_suspension = new TextField("Quantité dispo. (bouteilles)", null, 6, TextField.NUMERIC);
    azithromycine_tab = new ChoiceGroup("Azithromycine (comprimé / gel).", ChoiceGroup.POPUP, choice, null);
    nb_azithromycine_tab = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    azithromycine_suspension = new ChoiceGroup("Suspension d'azithromycine", ChoiceGroup.POPUP, choice, null);
    nb_azithromycine_suspension = new TextField("Quantité dispo. (bouteilles)", null, 6, TextField.NUMERIC);
    benzathine_penicillin = new ChoiceGroup("Benzathine pénicilline", ChoiceGroup.POPUP, choice, null);
    nb_benzathine_penicillin = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);
    cefexime = new ChoiceGroup("Céfixime", ChoiceGroup.POPUP, choice, null);
    nb_cefexime = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    clotrimazole = new ChoiceGroup("Clotrimazole", ChoiceGroup.POPUP, choice, null);
    nb_clotrimazole = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    ergometrine_tab = new ChoiceGroup("Ergométrine (comprimé)", ChoiceGroup.POPUP, choice, null);
    nb_ergometrine_tab = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);
    ergometrine_vials = new ChoiceGroup("Ergométrine (flacons)", ChoiceGroup.POPUP, choice, null);
    nb_ergometrine_vials = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    iron = new ChoiceGroup("Fer", ChoiceGroup.POPUP, choice, null);
    nb_iron = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    folate = new ChoiceGroup("acide folique", ChoiceGroup.POPUP, choice, null);
    nb_folate = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    iron_folate = new ChoiceGroup("Fer / acide folique", ChoiceGroup.POPUP, choice, null);
    nb_iron_folate = new TextField("Quantité dispo. (comprimés)", null, 6, TextField.NUMERIC);
    magnesium_sulfate = new ChoiceGroup("Sulfate de magnésium", ChoiceGroup.POPUP, choice, null);
    nb_magnesium_sulfate = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);
    metronidazole = new ChoiceGroup("Sétronidazole", ChoiceGroup.POPUP, choice, null);
    nb_metronidazole = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);
    oxytocine = new ChoiceGroup("Ocytocine", ChoiceGroup.POPUP, choice, null);
    nb_oxytocine = new TextField("Quantité dispo. (flacons)", null, 6, TextField.NUMERIC);

    append(reporting_year);
    append(reporting_month);
    append("¤ Service proposé");
    append(family_planning);
    append(delivery_services);
    append("¤ Methode de contraception proposé");
    append(male_condom);
    append(nb_male_condom);
    append(female_condom);
    append(nb_female_condom);
    append(oral_pills);
    append(nb_oral_pills);
    append(injectable);
    append(nb_injectable);
    append(iud);
    append(nb_iud);
    append(implants);
    append(nb_implants);
    append(female_sterilization);
    append(male_sterilization);
    append("¤ Disponibilité de medicament materno");
    append(amoxicillin_ij);
    append(nb_amoxicillin_ij);
    append(amoxicillin_cap_gel);
    append(nb_amoxicillin_cap_gel);
    append(amoxicillin_suspension);
    append(nb_amoxicillin_suspension);
    append(azithromycine_tab);
    append(nb_azithromycine_tab);
    append(azithromycine_suspension);
    append(nb_azithromycine_suspension);
    append(benzathine_penicillin);
    append(nb_benzathine_penicillin);
    append(cefexime);
    append(nb_cefexime);
    append(clotrimazole);
    append(nb_clotrimazole);
    append(ergometrine_tab);
    append(nb_ergometrine_tab);
    append(ergometrine_vials);
    append(nb_ergometrine_vials);
    append(iron);
    append(nb_iron);
    append(folate);
    append(nb_folate);
    append(iron_folate);
    append(nb_iron_folate);
    append(magnesium_sulfate);
    append(nb_magnesium_sulfate);
    append(metronidazole);
    append(nb_metronidazole);
    append(oxytocine);
    append(nb_oxytocine);

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
        // all fields are required to be filled.
        return true;
    }

    /*
     * Whether all filled data is correct
     * @return <code>true</code> if all fields are OK
     * <code>false</code> otherwise.
     */

    public boolean isValid() {
         if (reporting_month.getSelectedIndex() == 0) {
             ErrorMessage = "Le mois n'est pas valide.";
             return false;
        }
        return true;
    }

   /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */

    public String toSMSFormat() {
        String sep = " ";
        String cscom_code = config.get("cscom_code");
        
        return "fnuap mps" + sep + reporting_year.getString(reporting_year.getSelectedIndex())
                           + sep + SharedChecks.addzero(reporting_month.getSelectedIndex())
                           + sep + cscom_code
                           + sep + family_planning.getSelectedIndex()
                           + sep + delivery_services.getSelectedIndex()
                           + sep + test_value(male_condom.getSelectedIndex(), nb_male_condom.getString())
                           + sep + test_value(female_condom.getSelectedIndex(), nb_female_condom.getString())
                           + sep + test_value(oral_pills.getSelectedIndex(), nb_oral_pills.getString())
                           + sep + test_value(injectable.getSelectedIndex(), nb_injectable.getString())
                           + sep + test_value(iud.getSelectedIndex(), nb_iud.getString())
                           + sep + test_value(implants.getSelectedIndex(), nb_implants.getString())
                           + sep + female_sterilization.getSelectedIndex()
                           + sep + male_sterilization.getSelectedIndex()
                           + sep + test_value(amoxicillin_ij.getSelectedIndex(), nb_amoxicillin_ij.getString())
                           + sep + test_value(amoxicillin_cap_gel.getSelectedIndex(), nb_amoxicillin_cap_gel.getString())
                           + sep + test_value(amoxicillin_suspension.getSelectedIndex(), nb_amoxicillin_suspension.getString())
                           + sep + test_value(azithromycine_tab.getSelectedIndex(), nb_azithromycine_tab.getString())
                           + sep + test_value(azithromycine_suspension.getSelectedIndex(), nb_azithromycine_suspension.getString())
                           + sep + test_value(benzathine_penicillin.getSelectedIndex(), nb_benzathine_penicillin.getString())
                           + sep + test_value(cefexime.getSelectedIndex(), nb_cefexime.getString())
                           + sep + test_value(clotrimazole.getSelectedIndex(), nb_clotrimazole.getString())
                           + sep + test_value(ergometrine_tab.getSelectedIndex(), nb_ergometrine_tab.getString())
                           + sep + test_value(ergometrine_vials.getSelectedIndex(), nb_ergometrine_vials.getString())
                           + sep + test_value(iron.getSelectedIndex(), nb_iron.getString())
                           + sep + test_value(folate.getSelectedIndex(), nb_folate.getString())
                           + sep + test_value(iron_folate.getSelectedIndex(), nb_iron_folate.getString())
                           + sep + test_value(magnesium_sulfate.getSelectedIndex(), nb_magnesium_sulfate.getString())
                           + sep + test_value(metronidazole.getSelectedIndex(), nb_metronidazole.getString())
                           + sep + test_value(oxytocine.getSelectedIndex(), nb_oxytocine.getString());
    }

    public String test_value(int c, String s) {
        if (c == 1 && s.length() != 0){
                return s;
        }
        else{
            return "-";
        }
    }

    public String toText() {

        return  "P]:" + reporting_month.getString(reporting_month.getSelectedIndex())
                + "/" + reporting_year.getString(reporting_year.getSelectedIndex());
    }
    
    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "commodities");
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
