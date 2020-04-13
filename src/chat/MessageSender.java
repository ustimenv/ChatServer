package chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MessageSender {
	String payload = "";
	
	ArrayList<InetAddress> recipientAddresses = new ArrayList<>();
	
	
	// special case for acknoledgement packets
	public MessageSender(InetAddress recipientAddress, char flag)
	{
		recipientAddresses.add(recipientAddress);
		payload  = String.valueOf(flag) + SessionKeeper.DELIMITER;
	}
	
	// broadcast packets
	public MessageSender(ArrayList<InetAddress> recipientAddresses, String... contents)
	{
		this.recipientAddresses = recipientAddresses;
		StringBuilder sb = new StringBuilder();
		for (String s : contents) {
			sb.append(s);
			sb.append(SessionKeeper.DELIMITER);
		}
		payload = sb.toString();
	}
	
	// 1-1 packet
	public MessageSender(InetAddress recipientAddress, String... contents)
	{
		this.recipientAddresses.add(recipientAddress);
		StringBuilder sb = new StringBuilder();
		for (String s : contents) {
			sb.append(s);
			sb.append(SessionKeeper.DELIMITER);
		}
		payload = sb.toString();
	}
	
	
	void send()
	{
		for (InetAddress recipientAddress : recipientAddresses) {
			try (Socket sendingSocket = new Socket(recipientAddress, 8080);
				 PrintWriter os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendingSocket.getOutputStream()))))
			{
				os.write(payload);
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
