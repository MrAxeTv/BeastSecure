package me.mraxetv.beastsecurity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Handeler extends BukkitRunnable {

    BufferedReader in;
    PrintWriter out;
    Socket client;
    BeastSecurity pl;

    public Handeler(BeastSecurity pl, Socket client) throws IOException {
        this.pl = pl;
        this.client = client;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);

    }

    @Override
    public void run() {

        try {
            while (true) {

                int r = new Random().nextInt(15);

                List<String> messages = new ArrayList<>();

                ChatColor c = ChatColor.values()[r];
                String str = in.readLine();
                String[] args = str.split(";");
                if (args.length == 1) {
                    messages.add(c + "=================== New Connection ====================== ");
                    String nonce = args[0];
                    messages.add(c + "Nonce ID: " + nonce);
                    messages.add(c + "Server Instance IP: " + client.getInetAddress());

                    if (pl.getConfig().getStringList("BlackList").contains(str)) {
                        messages.add(c + "Disabling leak version!");
                        out.println(true);
                    } else {
                        messages.add(c + "Legit ID Version!");
                        out.println(false);
                    }
                    messages.add(c + "========================================= ");
                } else if (args.length == 5) {

                    messages.add(c + "=================== New Connection ====================== ");
                    String nonce = args[0];
                    String domainIP = args[1];
                    String userID = args[2];
                    String version = args[3];
                    String pluginID = args[4];
                    messages.add(c + "Nonce ID: " + nonce);
                    //messages.add(c + "Server Instance IP: " + client.getInetAddress() + " Port: " + client.getPort());
                    messages.add(c + "Server Instance IP: " + client.getInetAddress());
                    messages.add(c + "Domain IP: " + domainIP);
                    messages.add(c + "User ID: " + userID);
                    messages.add(c + "Plugin Version: " + version);
                    messages.add(c + "Plugin ID: " + pluginID);

                    if (pl.getConfig().getStringList("BlackList").contains(nonce)) {
                        messages.add(c + "Disabling leak version!");
                        out.println(true);
                    } else {
                        messages.add(c + "Legit ID Version!");
                        out.println(false);
                    }
                    messages.add(c + "========================================= ");
                } else {

                }


                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!BeastSecurity.getInstance().debug) return;
                        for (String s : messages) {
                            Bukkit.getServer().getConsoleSender().sendMessage(s);
                        }
                    }

                }.runTask(BeastSecurity.getInstance());

                break;


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                // Bukkit.getServer().getConsoleSender().sendMessage("Closed In");

            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();

            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Bukkit.getServer().getConsoleSender().sendMessage("Closed Out");
        }
    }
}
