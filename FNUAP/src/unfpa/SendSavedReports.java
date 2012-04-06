package unfpa;

import javax.microedition.lcdui.*;
import unfpa.SMSStore.*;
import unfpa.StoredSMS.*;

/*
 * J2ME Midlet allowing user to fill and submit UNFPA Forms
 */
public class SendSavedReports extends List implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.EXIT, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 5);

    public UNFPAMIDlet midlet;
    private Configuration config;
    private SMSStore store;

    public SendSavedReports(UNFPAMIDlet midlet) {
        super("Formulaires enregistrés", Choice.IMPLICIT);
        this.midlet = midlet;

        store = new SMSStore();
        StoredSMS[] all_sms = store.getAll();

        for (int i=0; i<all_sms.length; i++) {
            append(all_sms[i].name, null);
        }

        append("reg", null);
        append("fad", null);

        setCommandListener (this);
        addCommand (CMD_EXIT);
        addCommand (CMD_HELP);
    }

    public void commandAction(Command c, Displayable s) {
        // if it originates from the MainMenu list
        if (s.equals (this)) {
            // and is a select command
            if (c == List.SELECT_COMMAND) {

                int index = ((List) s).getSelectedIndex ();
                System.out.println(index);
            }
        }

        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "saved_reports");
            this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }
    }
}