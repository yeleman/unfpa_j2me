
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

public class HelpForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                        Command.BACK, 1);

    private StringItem helpText;
    UNFPAMIDlet midlet;
    Displayable returnTo;


    public HelpForm(UNFPAMIDlet midlet, Displayable d, String section) {
        super("Aide");
        this.midlet = midlet;
        this.returnTo = d;

        this.getContentFromSection(section);

        append(helpText);
        addCommand(CMD_EXIT);
        this.setCommandListener (this);
      }


    private void getContentFromSection(String section) {
        String text;

        if (section.equalsIgnoreCase("mainmenu")) {
            text = "Chaque élément de la liste correspond à un formulaire.\n" +
                   "Entrez dans celui qui correspond à votre opération puis " +
                   "renseignez les champs et envoyez.\n" +
                   "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("version")) {
            text = "FNUAP - Version " + Constants.version + "\n\n" +
                   "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("under5")) {
            text = "Renseignez uniquement la Mortalité infantile.\n" +
               "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("maternal")) {
            text = "Renseignez uniquement la Mortalité maternelle.\n" +
               "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("commodities")) {
            text = "Renseignez uniquement Commodities.\n" +
               "En cas de problème, contactez l'ANTIM.";
        } else {
            text = "Aucune aide disponible pour cet élément.";
        }
        helpText = new StringItem(null, text);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.returnTo);
        }
    }

}
