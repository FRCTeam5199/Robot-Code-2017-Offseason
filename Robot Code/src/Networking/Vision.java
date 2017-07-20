package Networking;

import com.jcraft.jsch.JSchException;

import Maths.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team5199.robot.Robot;

public class Vision implements Runnable {

	private static int PORT = 6969;
	private final Thread t;
	private String data = "0:0";
	private boolean isAlive;

	public Vision() {
		t = new Thread(this, "Vision Thread");
	}

	public void start() {
		Robot.nBroadcaster.println("Starting Vision");
		isAlive = true;
		t.start();
	}

	public void stop() {
		isAlive = false;
	}

	@Override
	public void run() {
		new Vision();

		String fromclient = null;

		ServerSocket Server = null;
		try {
			Server = new ServerSocket(PORT);
		} catch (IOException e) {
			Robot.nBroadcaster.println("Error 1 Failed to initialize vision port");
			e.printStackTrace();
		}

		// status.setText("Server Waiting for client on port " + PORT);
		Robot.nBroadcaster.println("Server waiting for client on port " + PORT);

		SSHThread BoardComm = new SSHThread();
		BoardComm.start();

		Socket connected = null;
		try {
			connected = Server.accept();
		} catch (IOException e) {
			Robot.nBroadcaster.println("Error 2 Failed to connect vision");
			e.printStackTrace();
		}
		// status.setText("THE CLIENT" + " " + connected.getInetAddress() + ":"
		// + connected.getPort() + " IS CONNECTED ");
		Robot.nBroadcaster
				.println("The client " + connected.getInetAddress() + ":" + connected.getPort() + " is connected");
		BufferedReader inFromClient = null;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
		} catch (IOException e) {
			Robot.nBroadcaster.println("Error 3 Failed to initialize vision BufferedReader");
			e.printStackTrace();
		}

		long lastUpdate;
		while (isAlive) {
			lastUpdate = System.currentTimeMillis();
			try {
				fromclient = inFromClient.readLine();
			} catch (IOException e) {
				Robot.nBroadcaster.println("Error 4 Failed to read data from client");
				e.printStackTrace();
			}
			if (fromclient == null) {
				// status.setText("Server Waiting for client on port " + PORT);
				try {
					Robot.nBroadcaster.println("Waiting for client on port " + PORT);
					connected = Server.accept();
				} catch (IOException e) {
					Robot.nBroadcaster.println("Error 5");
					e.printStackTrace();
				}
				// status.setText(
				// "THE CLIENT" + " " + connected.getInetAddress() + ":" +
				// connected.getPort() + " IS CONNECTED ");
				Robot.nBroadcaster.println("Connected to " + connected.getInetAddress() + ":" + connected.getPort());
				try {
					inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
				} catch (IOException e) {
					Robot.nBroadcaster.println("Error 6");
					e.printStackTrace();
				}

			} else {
				// display.setText(fromclient);
				data = fromclient;
			}
			Robot.nBroadcaster.println(System.currentTimeMillis()-lastUpdate);
		}

	}

	public Vector2 getPos() {
		String[] substrings = data.split(":");
		return new Vector2(Double.valueOf(substrings[0]), Double.valueOf(substrings[1]));
	}

}

class SSHThread extends Thread {

	public void run() {
		commandSSH ssh = new commandSSH("linaro", "10.51.99.13", 22, "linaro");
		try {
			String returned = ssh.sendCommand("python Desktop/SendCVTest.py 10.51.99.21 50 128 128 85 255 255");
			System.out.println(returned);
		} catch (JSchException | IOException ex) {
			ex.printStackTrace();
		}
	}
}
