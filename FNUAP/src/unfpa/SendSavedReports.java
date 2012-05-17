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
    private static final Command CMD_SEND_ALL = new Command ("Tout envoyer.", Command.OK, 1);
    private static final Command CMD_DELETE = new Command ("Supprimer.", Command.OK, 1);

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
            append(store.get(i).name, null);
//            append(store.get(i).name, null);
        }

        setCommandListener (this);
        addCommand (CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand(CMD_SEND_ALL);
        addCommand (CMD_DELETE);
        addCommand (CMD_HELP);
    }

    public void commandAction(Command c, Displayable s) {
        // if it originates from the MainMenu list
        if (s.equals (this)) {

            // help command displays Help Form.
            if (c == CMD_HELP) {
                HelpForm h = new HelpForm(this.midlet, this, "saved_reports");
                this.midlet.display.setCurrent(h);
            }
            // save or send all command
            if (c == CMD_SEND_ALL) {
                Alert alert;
                alert = new Alert ("");

                SMSSender sms = new SMSSender();
                String number = config.get("server_number");
                
                for (int i=0; i<all_sms.length; i++) {
                    if (sms.send(number, this.all_sms[i].sms)) {
                    //if (1==0){
                        alert = new Alert ("Demande envoyée !", "Vous allez recevoir" +
                                           " une confirmation du serveur.",
                                           null, AlertType.CONFIRMATION);
                        store.delete(i);
                    }else{
                        alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                                           " la demande par SMS. Le rapport a été enregistré dans le téléphone.", null,
                                           AlertType.WARNING);
                    }
                }
                this.midlet.display.setCurrent(alert, this.midlet.mainMenu);
            }

            // save or send command
            if (c == CMD_SEND) {
                int index = ((List) s).getSelectedIndex ();
                Alert alert;
                
                // sends the sms and reply feedback
                SMSSender sms = new SMSSender();
                String number = config.get("server_number");

                if (sms.send(number, this.all_sms[index].sms)) {
                    store.delete(index);
                    alert = new Alert ("Demande envoyée !", "Vous allez recevoir" +
                                       " une confirmation du serveur.",
                                       null, AlertType.CONFIRMATION);
                }else{
                    alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                                       " la demande par SMS. Le rapport a été enregistré dans le téléphone.", null,
                                       AlertType.WARNING);
                }
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            }

            // exit commands comes back to main menu.
            if (c == CMD_DELETE) {

                int index = ((List) s).getSelectedIndex ();
                Alert alert;
                
                if (this.store.delete(index)) {
                    alert = new Alert ("Suppression SMS", "Sms sélectionné a été supprimé.", null, AlertType.CONFIRMATION);
                } else
                    alert = new Alert ("Suppression SMS", "Échec de suppression.", null, AlertType.WARNING);
           
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);              
           }
            
           if (c == CMD_EXIT) 
                this.midlet.startApp();
        }
    }
}