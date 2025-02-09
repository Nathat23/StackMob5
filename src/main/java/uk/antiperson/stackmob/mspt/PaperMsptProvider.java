package uk.antiperson.stackmob.mspt;

import org.bukkit.Bukkit;

public class PaperMsptProvider extends MsptProvider {

    @Override
    public double getMspt() {
        return Bukkit.getAverageTickTime();
    }
}
