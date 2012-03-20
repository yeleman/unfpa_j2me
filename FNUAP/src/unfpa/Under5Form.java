
package unfpa;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import unfpa.Configuration.*;
import unfpa.Constants.*;

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

    private Configuration config;

     //register
    private DateField reporting_date;
    private ChoiceGroup code;
    private ChoiceGroup code1;
    private TextField name;
    private DateField dob;
    private DateField dod;
    private static final String[] codeList= {"kati", "kkro"};

    public Under5Form(UNFPAMIDlet midlet) {
        super("Mortalité moin de 5ans");
        this.midlet = midlet;

        config = new Configuration();

        reporting_date =  new DateField("Date de rapportage:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        code = new ChoiceGroup("Code village:", ChoiceGroup.POPUP, codeList, null);
        name = new TextField("Nom et prenom", null, 20, TextField.ANY);
        dob =  new DateField("Date de naussnce:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dod =  new DateField("Date de la mort:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        code1 = new ChoiceGroup("Code village:", ChoiceGroup.POPUP, codeList, null);
        append(reporting_date);
        append(code);
        append(name);
        append(dob);
        append(dod);
        append(code1);
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
        return true;
    }
    
    public String toSMSFormat() {
        return "moins de 5ans";
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

            alert = new Alert ("Message", "Alou est un boss", null, AlertType.WARNING);
            this.midlet.display.setCurrent (alert, this);

        }
    }
}
