package sockettest;

import com.jcraft.jsch.JSchException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SocketTest {

    private static int PORT = 6969;
    private static JLabel display, status;

    public SocketTest() {

        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        frame.add(mainPanel);
        display = new JLabel();
        status = new JLabel();
        mainPanel.add(status);
        mainPanel.add(display);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 75);
        frame.setResizable(false);

        frame.setVisible(true);

    }

    public static void main(String args[]) throws IOException {

        new SocketTest();

        String fromclient;

        ServerSocket Server = new ServerSocket(PORT);

        status.setText("Server Waiting for client on port " + PORT);

        thread BoardComm = new thread();   
        BoardComm.start();
        
        Socket connected = Server.accept();
        status.setText("THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));

        while (true) {

            fromclient = inFromClient.readLine();
            if (fromclient == null) {
                status.setText("Server Waiting for client on port " + PORT);
                connected = Server.accept();
                status.setText("THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
                inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));

            } else {
                display.setText(fromclient);

            }
        }
    }

}

class thread extends Thread {

    public void run() {
        commandSSH ssh = new commandSSH("linaro", "10.51.99.13", 22, "linaro");
        try {
            String returned = ssh.sendCommand("python Desktop/SendCVTest.py 10.51.99.217");
            System.out.println(returned);
        } catch (JSchException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
