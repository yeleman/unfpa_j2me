
package unfpa;

import javax.microedition.lcdui.*;
import unfpa.Configuration.*;
import unfpa.HelpForm.*;
import unfpa.SMSStore.*;
import unfpa.StoredSMS.*;

/*
 * @author fadiga
 */

public class SendSaveForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    UNFPAMIDlet midlet;
    Displayable returnTo;
    private Configuration config;
    public List mainMenu;
    private SMSStore store;

    public List all_sms;

    public SendSaveForm(UNFPAMIDlet midlet) {
        super("send save");
        this.midlet = midlet;
        SMSStore store = new SMSStore();

        String[] mainMenu_items = {"Mortalité infantile", "Mortalité maternelle", "Dispo. Produits", "Envoi form. ("+ store.count() +")"};
        mainMenu = new List("Formulaires FNUAP", Choice.IMPLICIT, mainMenu_items, null);


        config = new Configuration();
        
        append(mainMenu_items);
        
        addCommand(CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand(CMD_HELP);
        //System.out.println(all_sms);

        this.setCommandListener (this);
    }

     public String toSMSFormat() {
              return " ";
    }
    public String toText() {
        return "E] ";
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "send save");
            this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command
        if (c == CMD_SEND) {
            Alert alert;

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
              alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                                 " la demande par SMS. Le rapport a été enregistré dans le téléphone.", null,
                                       AlertType.WARNING);
              this.midlet.display.setCurrent (alert, this);
            }

        }
    }
}
