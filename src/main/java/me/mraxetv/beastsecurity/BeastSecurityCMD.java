package me.mraxetv.beastsecurity;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class BeastSecurityCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(args.length == 1 && args[0].equalsIgnoreCase("debug")){

            if(BeastSecurity.getInstance().debug){
                BeastSecurity.getInstance().debug = false;
                sender.sendMessage("[BeastSecurity] DEBUG: Disabled");
            }
            else {
                sender.sendMessage("[BeastSecurity] DEBUG: Enabled");
                BeastSecurity.getInstance().debug = true;
            }

            return false;
        }
        if (args.length == 2) {
            if(args[0].equalsIgnoreCase("addnonce")){
                List<String> list = BeastSecurity.getInstance().getConfig().getStringList("BlackList");
                if(list.contains(args[1])){
                    sender.sendMessage("[BeastSecurity] Nonce ID("+args[1]+") is already on BlackList!");
                    return false;
                }
                list.add(args[1]);
                BeastSecurity.getInstance().getConfig().set("BlackList",list);
                BeastSecurity.getInstance().saveConfig();
                sender.sendMessage("[BeastSecurity] Nonce ID("+args[1]+") has been added!");
            }
            if(args[0].equalsIgnoreCase("removenonce")){
                List<String> list = BeastSecurity.getInstance().getConfig().getStringList("BlackList");
                if(!list.contains(args[1])){
                    sender.sendMessage("[BeastSecurity] Nonce ID("+args[1]+") is not on the BlackList!");
                    return false;
                }
                list.remove(args[1]);
                BeastSecurity.getInstance().getConfig().set("BlackList",list);
                BeastSecurity.getInstance().saveConfig();
                sender.sendMessage("[BeastSecurity] Nonce ID("+args[1]+") has been removed!");
            }
            return false;
        }


        sender.sendMessage("[BeastSecurity] /BeastSecurity debug");
        sender.sendMessage("[BeastSecurity] /BeastSecurity addNonce {nonce}");
        sender.sendMessage("[BeastSecurity] /BeastSecurity removeNonce {nonce}");


        return false;
    }
}
