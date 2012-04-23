
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import unfpa.Configuration.*;
import unfpa.Constants.*;
import unfpa.SharedChecks.*;
import java.util.Date;


/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author alou/fadiga
 */

public class BirthForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    UNFPAMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    //register
    private DateField reporting_date;
    private TextField householder;
    private TextField name_mother;
    private TextField name_father;
    private TextField name_child;
    private static final String[] sexList= {"F", "M"};
    private ChoiceGroup sex;
    private static final String[] YesNon = {"OUI", "NON"};
    private static final String[] TypeLocation = {"Domicile", "Centre", "Autre"};
    private TextField other;
    private ChoiceGroup born_alive;
    private ChoiceGroup location;
    private DateField dob;
    private TextField age;
    private DateField dod;
    Date now = new Date();
    String sep = " ";

    public BirthForm(UNFPAMIDlet midlet) {
        super("Données de naissance");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        //date
        reporting_date =  new DateField("Date d'enregistrement", DateField.DATE, TimeZone.getTimeZone("GMT"));
        reporting_date.setDate(now);
        dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dob.setDate(now);

        //text
        householder = new TextField("Chef de menage", null, 20, TextField.ANY);
        name_mother = new TextField("Nom de la mère", null, 20, TextField.ANY);
        name_father = new TextField("Nom du père", null, 20, TextField.ANY);
        name_child = new TextField("Nom de l'enfant", null, 20, TextField.ANY);
        other = new TextField("Précision", null, 20, TextField.ANY);
        age =  new TextField("Age (DDN inconnue):", null, Constants.AGE_STR_MAX, TextField.ANY);

        //choice
        location = new ChoiceGroup("Lieu de Naissance:", ChoiceGroup.POPUP, TypeLocation, null);
        sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
        born_alive = new ChoiceGroup("Né vivant:", ChoiceGroup.POPUP, YesNon, null);
        

        append(householder);
        append(reporting_date);
        append(name_father);
        append(name_mother);
        append(name_child);
        append(age);
        append(dob);
        append(location);
        append(other);
        append(sex);
        append(born_alive);

        addCommand(CMD_EXIT);
        addCommand(CMD_SAVE);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // TODO: ajouter tous les champs.

        // all fields are required to be filled.
        if (householder.getString().length() == 0||
            name_father.getString().length() == 0||
            name_mother.getString().length() == 0||
            name_child.getString().length() == 0) {
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

        return true;
    }

    public String AddZero(int num){
        String snum = "";
        if (num < 10)
            snum = "0" + num;
        else
            snum = snum + num;
        return snum;
    }

    public String toSMSFormat() {

        String fdob;
        String loc;
        int born;

        int reporting_date_array[] = SharedChecks.formatDateString(reporting_date.getDate());
        int reporting_date_day = reporting_date_array[0];
        int reporting_date_month = reporting_date_array[1];
        int reporting_date_year = reporting_date_array[2];

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        int dob_day = dob_array[0];
        int dob_month = dob_array[1];
        int dob_year = dob_array[2];

        if (age.getString().length() != 0)
            fdob = age.getString();
        else
            fdob = dob_year + AddZero(dob_month) + AddZero(dob_day);

        if (location.getString(location.getSelectedIndex()).equals("Domicile"))
            loc = "D";
        else if (location.getString(location.getSelectedIndex()).equals("Centre"))
            loc = "C";
        else
            loc = other.getString();

        if (born_alive.getString(born_alive.getSelectedIndex()).equals("OUI"))
            born = 1;
        else
            born = 0;

        System.out.print("alou");
        return "fnuap born" + sep + reporting_date_year +  AddZero(reporting_date_month)
                           + AddZero(reporting_date_day) + sep
                           + householder.getString() + sep
                           + name_father.getString() + sep
                           + name_mother.getString() + sep
                           + name_child.getString() + sep
                           + fdob + sep
                           + loc + sep
                           + sex.getString(sex.getSelectedIndex()) + sep
                           + born;
    }

    public String toText() {

        int dob_array[] = SharedChecks.formatDateString(dob.getDate());
        int dob_day = dob_array[0];
        int dob_month = dob_array[1];
        int dob_year = dob_array[2];

        return "N] " + householder.getString() + sep + dob_year + "/"
                            + dob_month + "/" + dob_day;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "born");
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
                // TODO: ajouter sauvegarde dans BDD.

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