package ro.steve.gens.commands;

import org.bukkit.command.CommandSender;
import ro.steve.gens.GensMain;
import ro.steve.gens.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class GensHelpCommand {

    public GensHelpCommand(CommandSender s) {
        var y = GensMain.getInstance().getConfiguration().getConfig("messages");
        List<String> help = new ArrayList<>();
        List<String> fromYml = y.getStringList("Help");
        fromYml.forEach(m -> {
            if (m.contains("{%require%}:")) {
                m = m.replace("{%require%}:", "");
                String[] split = m.split(" ");
                if (s.hasPermission(split[0])) {
                    StringBuilder b = new StringBuilder();
                    for (int i = 1; i < split.length; i++) {
                        b.append(split[i]).append(" ");
                    }
                    help.add(Color.process(b.toString()));
                }
                return;
            }
            help.add(Color.process(m));
        });
        help.forEach(s::sendMessage);
    }
}
