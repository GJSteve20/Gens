package ro.steve.gens.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.steve.gens.GensMain;
import ro.steve.gens.inventory.GensInventory;

public class GensUpgradeCommand {

    public GensUpgradeCommand(CommandSender sender) {
        var p = ((Player) sender);
        var s = GensMain.getInstance().getStorage();
        if (p.getTargetBlockExact(10) == null) {
            return;
        }
        System.out.println(s.isGen(p.getTargetBlockExact(10).getLocation()));
        if (!s.isGen(p.getTargetBlockExact(10).getLocation())) {
            //nope
            return;
        }
        p.openInventory(new GensInventory("Upgrade", p.getTargetBlockExact(10).getLocation()).getInventory());
    }
}
