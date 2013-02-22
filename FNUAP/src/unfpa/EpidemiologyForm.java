
package unfpa;

import javax.microedition.lcdui.*;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.Entities.*;
import unfpa.SharedChecks.*;
import java.util.Date;
import java.util.Hashtable;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author fadiga
 */


public class EpidemiologyForm extends Form implements CommandListener {

    private static final int MAX_SIZE = 5;
    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    UNFPAMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private static final String[] year_list = {"2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};

    private static final String[] order_maladies = {
        Constants.acute_flaccid_paralysis,  Constants.influenza_a_h1n1, Constants.cholera,
        Constants.red_diarrhea, Constants.measles, Constants.yellow_fever,
        Constants.neonatal_tetanus, Constants.meningitis,
        Constants.rabies, Constants.acute_measles_diarrhea, Constants.other_notifiable_disease
    };
    private ChoiceGroup yearfield;
    private TextField week_numberfield;
    private Configuration config;
    private SMSStore store;

    Date now = new Date();
    String sep = " ";

    private Hashtable cap_fields;
    private Hashtable indiv_fields;
    private Hashtable maladie_list;

    public EpidemiologyForm(UNFPAMIDlet midlet) {
        super("Données Epidémiologique");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        yearfield = new ChoiceGroup("Année:", ChoiceGroup.POPUP, year_list, null);
        week_numberfield = new TextField("Sémaine:", null, 2, TextField.NUMERIC);

        append(yearfield);
        append(week_numberfield);

        maladie_list = new Hashtable();
        maladie_list.put(Constants.acute_flaccid_paralysis, "PFA:");
        maladie_list.put(Constants.influenza_a_h1n1, "Grippe A H1N1:");
        maladie_list.put(Constants.cholera, "Choléra:");
        maladie_list.put(Constants.red_diarrhea, "Diarrhéé rouge:");
        maladie_list.put(Constants.measles, "Rougeole:");
        maladie_list.put(Constants.yellow_fever, "Fievere jaune:");
        maladie_list.put(Constants.neonatal_tetanus, "TNN:");
        maladie_list.put(Constants.meningitis, "Meningite:");
        maladie_list.put(Constants.rabies, "Rage:");
        maladie_list.put(Constants.acute_measles_diarrhea, "Diarrhée severe rougeole:");
        maladie_list.put(Constants.other_notifiable_disease, "Autres MADOS:");

        cap_fields = new Hashtable();
        for(int i=0;i<order_maladies.length;i++) {
            String code = order_maladies[i];
            indiv_fields = new Hashtable();
            String namefield = (String)maladie_list.get(code);

            TextField cas = new TextField("Cas:", null, MAX_SIZE, TextField.NUMERIC);
            TextField deces = new TextField("Décès:", null, MAX_SIZE, TextField.NUMERIC);
            append(namefield);
            append(cas);
            append(deces);
            indiv_fields.put("cas", cas);
            indiv_fields.put("deces", deces);

            cap_fields.put(code, indiv_fields);
        }
        append("Fin du formulaire");
        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {
        for(int i=0;i<order_maladies.length;i++) {
            String code = order_maladies[i];

            indiv_fields = (Hashtable)cap_fields.get(code);
            String namefield = (String)maladie_list.get(code);

            TextField casfield = (TextField)indiv_fields.get("cas");
            TextField decesfield = (TextField)indiv_fields.get("deces");
            if (casfield.getString().length() == 0 || decesfield.getString().length() == 0){
                ErrorMessage = '[' + namefield + ']' + " le cas et le décès ne peut pas être vide";
                return false;
           }
           if (week_numberfield.getString().length() == 0){
                ErrorMessage = "[Sémaine:] ne peut pas être vide";
                return false;
           }
        }
        return true;
    }

    public boolean isValid() {

        int year = Integer.parseInt(yearfield.getString(yearfield.getSelectedIndex()));
        int week_number = Integer.parseInt(week_numberfield.getString());

        Date now = new Date();
        int array[] = SharedChecks.formatDateString(now);
        int now_year = array[2];

        if (week_number > 52){

            ErrorMessage = "Il n'y a que 52 sémaines dans l'année.";
            return false;
        }

        if (week_number == 0){
            ErrorMessage = "Il n'y a pas de sémaine zéro";
            return false;
        }

        if (now_year < year){
            ErrorMessage = "L'année est dans le futur.";
            return false;
        }

        for(int i=0;i<order_maladies.length;i++) {
            String code = order_maladies[i];

            indiv_fields = (Hashtable)cap_fields.get(code);
            String namefield = (String)maladie_list.get(code);

            TextField casfield = (TextField)indiv_fields.get("cas");
            TextField decesfield = (TextField)indiv_fields.get("deces");
            if (Integer.parseInt(casfield.getString()) < Integer.parseInt(decesfield.getString())){
                ErrorMessage = '[' + namefield + ']' + "Le nombre de cas ne peut pas être inférieur au nombre de décès";
                 return false;
             }
        }
        return true;
    }

    public String toSMSFormat() {
        String msg = "";
        String sep = " ";

        for(int i=0;i<order_maladies.length;i++) {
            String code = order_maladies[i];
            indiv_fields = (Hashtable)cap_fields.get(code);

            TextField casfield = (TextField)indiv_fields.get("cas");
            TextField decesfield = (TextField)indiv_fields.get("deces");
            msg += sep + casfield.getString() + sep + decesfield.getString();
        }

        String prof = SharedChecks.profile();
        String cscom_code = config.get("cscom_code");

        return "fnuap epid" + sep + prof
                            + sep + yearfield.getString(yearfield.getSelectedIndex())
                            + sep + week_numberfield.getString()
                            + sep + cscom_code
                            + msg;
   }

    public String toText() {
        return "E-" + week_numberfield.getString() + yearfield.getString(yearfield.getSelectedIndex());
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "epid");
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
                alert = new Alert("Données manquantes", this.ErrorMessage, null,
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
