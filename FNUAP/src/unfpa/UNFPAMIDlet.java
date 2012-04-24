package unfpa;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import unfpa.OptionForm.*;
import unfpa.Under5Form.*;
import unfpa.MaternalMortalityrForm.*;
import unfpa.CommoditiesForm.*;
import unfpa.SMSStore.*;
import unfpa.StoredSMS.*;
import unfpa.PregnancyForm.*;
import unfpa.SendSavedReports.*;

/*
 * J2ME Midlet allowing user to fill and submit UNFPA Forms
 */
public class UNFPAMIDlet extends MIDlet implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Quitter", Command.EXIT, 1);
    private static final Command CMD_VERSION = new Command ("Version", Command.SCREEN, 2);
    private static final Command CMD_SRVNUM = new Command ("Configuration", Command.SCREEN, 4);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 5);

    public Display display;
    public List mainMenu;
    private Configuration config;

    public UNFPAMIDlet() {
        display = Display.getDisplay(this);
    }

    public void startApp() {

        config = new Configuration();
        SMSStore store = new SMSStore();

        String[] mainMenu_items = {"Mortalité infantile", "Mortalité maternelle", "Dispo. Produits", "Grossesse form", "Envoi form. ("+ store.count() +")"};
        mainMenu = new List("Formulaires FNUAP", Choice.IMPLICIT, mainMenu_items, null);

        // setup menu
        mainMenu.setCommandListener (this);
        mainMenu.addCommand (CMD_EXIT);
        mainMenu.addCommand (CMD_HELP);
        mainMenu.addCommand (CMD_VERSION);
        mainMenu.addCommand (CMD_SRVNUM);

       display.setCurrent(mainMenu);

    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {

        // if it originates from the MainMenu list
        if (s.equals (mainMenu)) {
            // and is a select command
            if (c == List.SELECT_COMMAND) {

                switch (((List) s).getSelectedIndex ()) {

                // Form under5
                case 0:
                    Under5Form u5_form = new Under5Form(this);
                    display.setCurrent (u5_form);
                    break;

		        // Form mother
                case 1:
                     MaternalMortalityrForm matmor_form = new MaternalMortalityrForm(this);
                     display.setCurrent (matmor_form);
                    break;

                // products
                case 2:
                    CommoditiesForm stock_form = new CommoditiesForm(this);
                    display.setCurrent (stock_form);
                    break;

                // submit stored messages
                case 3:
                    PregnancyForm pregnancy_reports = new PregnancyForm(this);
                    display.setCurrent (pregnancy_reports);
                    break;

                // submit stored messages
                case 4:
                    SendSavedReports saved_reports = new SendSavedReports(this);
                    display.setCurrent (saved_reports);
                    break;

                }
            }
        }

        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this, this.mainMenu, "mainmenu");
            display.setCurrent(h);
        }

        // version command displays Help Form for "version"
        if (c == CMD_VERSION) {
            HelpForm v = new HelpForm(this, this.mainMenu, "version");
            display.setCurrent(v);
        }

        // srvnum command displays Edit Number Form.
        if (c == CMD_SRVNUM) {
            OptionForm f = new OptionForm(this);
            display.setCurrent(f);
        }

        // exit commands exits application completely.
        if (c == CMD_EXIT) {
            destroyApp(false);
            notifyDestroyed();
        }
    }
}