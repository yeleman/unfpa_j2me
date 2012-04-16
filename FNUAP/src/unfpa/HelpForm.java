
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
            text = "Ouvrer le formulaire qui correspond à votre opération puis " +
                   "renseignez les champs et envoyez.\n" +
                   "Un SMS non envoyé est sauvegardé dans <<Envoi form>>" +
                   "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("version")) {
            text = "FNUAP - Version " + Constants.version + "\n\n" +
                   "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("under5")) {
            text = "Si vous connaissez la date de naissance, indiquez la, sinon indiquez l'age dans les formats suivants 5a(5ans) ou 5m(5mois).\n" +
                "Indiquez le code du village où a eu lieu la visite.\n" +
                "Indiquez le code du village où a eu lieu le décès.\n" +
                "Les codes de villages sont fourni à l'annexe.\n" +
                "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("maternal")) {
            text = "Si vous connaissez la date de naissance, indiquez la, sinon indiquez l'age dans les formats suivants 18a(18ans) ou 216m(216mois).\n" +
                "Indiquez le code du village où a eu lieu la visite.\n" +
                "Indiquez le code du village où a eu lieu le décès.\n" +
                "Les codes de villages sont fourni à l'annexe.\n" +
                "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("saved_reports")) {
            text = "Vous pouvez envoyer tous les SMS à la fois ou le faire un à un.\n" +
                "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("commodities")) {
            text = "Si la structure fournit des produits selectionnez oui et dans le champ dans bas indiquez la quantité.\n" +
                "La période concernée est le mois precedant" +
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
