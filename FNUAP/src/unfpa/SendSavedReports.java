package unfpa;

import javax.microedition.lcdui.*;
import unfpa.SMSStore.*;
import unfpa.Configuration.*;
import unfpa.StoredSMS.*;

/*
 * J2ME Midlet allowing user to fill and submit UNFPA Forms
 */
public class SendSavedReports extends List implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.EXIT, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 5);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);

    public UNFPAMIDlet midlet;
    private Configuration config;
    private SMSStore store;
    private StoredSMS[] all_sms;

    public SendSavedReports(UNFPAMIDlet midlet) {
        super("Formulaires enregistrés", Choice.IMPLICIT);
        this.midlet = midlet;

        store = new SMSStore();
        all_sms = store.getAll();
        config = new Configuration();

        for (int i=0; i<all_sms.length; i++) {
            append(all_sms[i].name, null);
        }

        append("reg", null);
        append("fad", null);

        setCommandListener (this);
        addCommand (CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand (CMD_HELP);
    }

    public void commandAction(Command c, Displayable s) {
         System.out.println(s);
          System.out.println(c);
        // if it originates from the MainMenu list
        if (s.equals (this)) {

            // help command displays Help Form.
            if (c == CMD_HELP) {
                HelpForm h = new HelpForm(this.midlet, this, "saved_reports");
                this.midlet.display.setCurrent(h);
            }
            // save command
            if (c == CMD_SEND) {
                System.out.println("send");
                int index = ((List) s).getSelectedIndex ();
                Alert alert;
                
                // sends the sms and reply feedback
                SMSSender sms = new SMSSender();
                String number = config.get("server_number");

                if (sms.send(number, this.all_sms[index].sms)) {
                    alert = new Alert ("Demande envoyée !", "Vous allez recevoir" +
                                       " une confirmation du serveur.",
                                       null, AlertType.CONFIRMATION);
                    store.delete(index);
                    this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
                }
            }

            // exit commands comes back to main menu.
            if (c == CMD_EXIT) {
                this.midlet.display.setCurrent(this.midlet.mainMenu);
            }
        }
    }
}