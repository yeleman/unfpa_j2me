
package unfpa;

import javax.microedition.lcdui.*;
import unfpa.Configuration.*;
import unfpa.Constants.*;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author alou/fadiga
 */

public class IndicatorForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    UNFPAMIDlet midlet;
    Displayable returnTo;

    private StringItem intro;

    //Services produisant des CAP
    private TextField iud;
    private TextField innjectable;
    private TextField oral_pills;
    private TextField male_condom;
    private TextField female_condom;
    private TextField emergency_contraception;
    private TextField implant;

    //Services ne produisant des CAP
    private TextField new_client;
    private TextField returning_client;
    private TextField pf_visit_u25;
    private TextField pf_visit_o25;
    private TextField pf_first_time;
    private TextField pf_visit_ams_ticket;
    private TextField pf_visit_provider_ticket;
    private TextField pf_visit_short_term;
    private TextField pf_visit_long_term;
    private TextField client_hiv_counselling;
    private TextField client_hiv_tested;
    private TextField client_hiv_positive;

    //Designation
    private TextField implant_removal;
    private TextField iud_removal;
    private TextField total_hiv_test;

    public IndicatorForm(UNFPAMIDlet midlet) {
        super("Formulaire du rapport mensuel des franchises");
        this.midlet = midlet;

        //Services produisant des CAP
        intro =  new StringItem(null, "Services produisant des cap");
        iud = new TextField("Dispositif intra-yterin (iudDIU)", null, 20, TextField.NUMERIC);
        innjectable = new TextField("Injectables - 3 mois", null, 20, TextField.NUMERIC);
        oral_pills = new TextField("Plaquettes pilules", null, 20, TextField.NUMERIC);
        male_condom = new TextField("Pièces de préservatifs masculins(payant)", null, 20, TextField.NUMERIC);
        female_condom = new TextField("Pièces de préservatifs feminins(payant)", null, 20, TextField.NUMERIC);
        emergency_contraception = new TextField("Comtraception d'urgence(payant)", null, 20, TextField.NUMERIC);
        implant = new TextField("Implants 5 ans", null, 20, TextField.NUMERIC);
        append(intro);
        append(iud);
        append(innjectable);
        append(oral_pills);
        append(male_condom);
        append(female_condom);
        append(emergency_contraception);
        append(implant);

        //Services ne produisant des CAP
        intro =  new StringItem(null, "Services ne produisant des cap");
        new_client = new TextField("Total nouveaux clients PF", null, 20, TextField.NUMERIC);
        returning_client = new TextField("Total anciens clients PF", null, 20, TextField.NUMERIC);
        pf_visit_u25 = new TextField("Nombre total de visites de clients âgés de 24 ans ou moins pour services de PF", null, 20, TextField.NUMERIC);
        pf_visit_o25 = new TextField("Nombre total de visites de clients âgés de 24 ans ou plus pour services de PF", null, 20, TextField.NUMERIC);
        pf_first_time = new TextField("Nombre total de clients utilisant une méthode de PF pour la prémière fois dans leur vie", null, 20, TextField.NUMERIC);
        pf_visit_ams_ticket = new TextField("Nombre total de visites de clients pour services de PF sur bon AMS", null, 20, TextField.NUMERIC);
        pf_visit_provider_ticket = new TextField("Nombre total de visites de clients pour services de PF sur bon Prestataire", null, 20, TextField.NUMERIC);
        pf_visit_short_term = new TextField("Nombre total de visites de clients pour methode de PF à court terme", null, 20, TextField.NUMERIC);
        pf_visit_long_term = new TextField("Nombre total de visites de clients pour methode de PF à long dure", null, 20, TextField.NUMERIC);
        client_hiv_counselling = new TextField("No. total de clients ayant beneficié du counseling VIH", null, 20, TextField.NUMERIC);
        client_hiv_tested = new TextField("Nombre ayant fait le test VIH", null, 20, TextField.NUMERIC);
        client_hiv_positive = new TextField("Nombre de clients dépistés séropositifs", null, 20, TextField.NUMERIC);
        append(intro);
        append(new_client);
        append(returning_client);
        append(pf_visit_u25);
        append(pf_visit_o25);
        append(pf_first_time);
        append(pf_visit_ams_ticket);
        append(pf_visit_provider_ticket);
        append(pf_visit_short_term);
        append(pf_visit_long_term);
        append(client_hiv_counselling);
        append(client_hiv_tested);
        append(client_hiv_positive);

        //Designation
        intro =  new StringItem(null, "Designation");
        implant_removal = new TextField("Retrait d'implant contraceptif", null, 20, TextField.ANY);
        iud_removal = new TextField("Retrait d'implant de DIU", null, 20, TextField.ANY);
        total_hiv_test = new TextField("Total test VIH", null, 20, TextField.ANY);

        append(intro);
        append(implant_removal);
        append(iud_removal);
        append(total_hiv_test);

        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);
        this.setCommandListener (this);
      }
    public boolean isComplete() {
        // all fields are required to be filled.
        if (iud.getString().length() == 0 ||
            innjectable.getString().length() == 0 ||
            oral_pills.getString().length() == 0 ||
            male_condom.getString().length() == 0 ||
            female_condom.getString().length() == 0 ||
            emergency_contraception.getString().length() == 0 ||
            new_client.getString().length() == 0 ||
            returning_client.getString().length() == 0 ||
            pf_visit_u25.getString().length() == 0 ||
            pf_visit_o25.getString().length() == 0 ||
            pf_first_time.getString().length() == 0 ||
            pf_visit_ams_ticket.getString().length() == 0 ||
            pf_visit_provider_ticket.getString().length() == 0 ||
            pf_visit_short_term.getString().length() == 0 ||
            pf_visit_long_term.getString().length() == 0 ||
            client_hiv_counselling.getString().length() == 0 ||
            client_hiv_tested.getString().length() == 0 ||
            client_hiv_positive.getString().length() == 0 ||
            implant_removal.getString().length() == 0 ||
            iud_removal.getString().length() == 0 ||
            total_hiv_test.getString().length() == 0) {
            return false;
        }
        return true;
    }
    public String toSMSFormat() {
        return "indicateurs";
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "indicateur");
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
                                 "requis doivent être remplis!", null, AlertType.ERROR);
                    alert.setTimeout(Alert.FOREVER);
                    this.midlet.display.setCurrent (alert, this);
                    return;
                }

            alert = new Alert ("Message", "Alou est un boss", null, AlertType.WARNING);
            this.midlet.display.setCurrent (alert, this);

        }
    }

}
