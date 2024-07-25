package ro.steve.gens.commands;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.steve.gens.GensMain;
import ro.steve.gens.inventory.GensInventory;
import ro.steve.gens.utils.SendMessage;

public class GensUpgradeCommand {

    public GensUpgradeCommand(CommandSender sender, Block block) {
        var p = ((Player) sender);
        var s = GensMain.getInstance().getStorage();
        Block b = null;
        if (block != null) {
            b = block;
        }
        if (p.getTargetBlockExact(10) != null) {
            b = p.getTargetBlockExact(10);
        }
        if (b == null) {
            SendMessage.sendMessage(p, "gen-not-valid");
            return;
        }
        if (!s.isGen(b.getLocation())) {
            SendMessage.sendMessage(p, "gen-not-valid");
            return;
        }
        if (s.getTier(p.getTargetBlockExact(10).getLocation()) == null) {
            SendMessage.sendMessage(p, "invalid-tier", "%tier%;" + s.getTier(p.getTargetBlockExact(10).getLocation()));
            return;
        }
        int t = (int) s.getTier(p.getTargetBlockExact(10).getLocation());
        var g = GensMain.getInstance().getConfiguration().getConfig("gens");
        if (g.get("Gens.tier_" + (t + 1)) == null) {
            SendMessage.sendMessage(p, "max-tier");
            return;
        }
        p.openInventory(new GensInventory("Upgrade", p.getTargetBlockExact(10).getLocation()).getInventory());
    }
}
