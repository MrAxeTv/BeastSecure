package me.mraxetv.beastsecurity;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BeastSecurity extends JavaPlugin  {

    ArrayList<Handeler> list;
    ServerSocket ss;
    ExecutorService pool;
    public static BeastSecurity INSTANCE;

    public boolean debug = true;


    @Override
    public void onEnable(){
        super.onEnable();
        saveDefaultConfig();
        list = new ArrayList<Handeler>();
        getCommand("BeastSecurity").setExecutor(new BeastSecurityCMD());
        INSTANCE =this;

        try {
            onStart();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    @Override
    public void onDisable(){
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStart() throws IOException{


        Bukkit.getScheduler().runTaskAsynchronously(INSTANCE, new Runnable() {
            @Override
            public void run() {
                pool = Executors.newFixedThreadPool(999);
                try {

                    //Code to show localhost address of machine
                    Enumeration e = NetworkInterface.getNetworkInterfaces();
                    while(e.hasMoreElements())
                    {
                        NetworkInterface n = (NetworkInterface) e.nextElement();
                        Enumeration ee = n.getInetAddresses();
                        while (ee.hasMoreElements())
                        {
                            InetAddress i = (InetAddress) ee.nextElement();
                            System.out.println(i.getHostAddress());
                        }
                    }

                    //InetAddress addr = InetAddress.getByName("172.17.0.57");
                    ss = new ServerSocket(4999);


                    Socket client = null;
                    while (true) {
                        client = ss.accept();
                        Handeler handeler = new Handeler(INSTANCE, client);
                        list.add(handeler);
                        pool.execute(handeler);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static BeastSecurity getInstance(){
        return INSTANCE;
    }

}
