/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author kevinjung
 */
public class commandSSH {

    private static Session session;
    private static ChannelExec channel;
    private static String user;
    private static String host;
    private static int port;

    public commandSSH(String user, String host, int port, String password) {

        commandSSH.user = user;
        commandSSH.host = host;
        commandSSH.port = port;

        try {

            JSch jsch = new JSch();
            session = jsch.getSession(commandSSH.user, commandSSH.host, commandSSH.port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            


        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    public static String sendCommand(String command) throws JSchException, IOException {
        
        System.out.println("Tyring to connect to " + host + ":" + port);
        session.connect();
        System.out.println("Connected");

        channel = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.setCommand(command);
        channel.connect();

        String out =  "";
        String msg = "";
        while ((msg = in.readLine()) != null) {
            out += msg+"\n";
        }
        
        channel.disconnect();
        session.disconnect();
        System.out.println("Disconnected");
        
        return out;
        
    }
}
