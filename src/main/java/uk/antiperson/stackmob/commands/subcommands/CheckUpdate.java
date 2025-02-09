package uk.antiperson.stackmob.commands.subcommands;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;
import uk.antiperson.stackmob.commands.User;

@CommandMetadata(command = "update", playerReq = false, desc = "Check for plugin updates.")
public class CheckUpdate extends SubCommand {

    private final StackMob sm;
    public CheckUpdate(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(User sender, String[] args) {
        sender.sendInfo("Contacting Modrinth. Please wait...");
        sm.getUpdater().checkUpdate().whenComplete((updateResult, throwable) -> {
           switch (updateResult.getResult()) {
               case ERROR:
                   sender.sendError("An error occurred while checking for updates.");
                   break;
               case NONE:
                   sender.sendInfo("There are no updates currently available.");
                   break;
               case AVAILABLE:
                   sender.sendSuccess("A new update (" + updateResult.getNewVersion() + ") is currently available!");
                   sender.sendSuccess("This can be downloaded using the command '/sm upgrade'");
                   break;
           }
        });
        return false;
    }
}
