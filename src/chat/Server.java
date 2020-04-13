package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server
{
	ServerSocket serverSocket;
	Socket receivingSocket;
	private final int ownPort;
	
	public Server()
	{
		ownPort = 50000;
		try
		{
			serverSocket = new ServerSocket(ownPort);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	private void listen()
	{
		ClientOperationsExecutor x = new ClientOperationsExecutor();
		int flag = -1;
		String input = "";
		while(true)
		{
			try
			{
				System.out.println("Awaiting input");
				receivingSocket = serverSocket.accept();
				System.out.println(receivingSocket.getInetAddress() + " " + receivingSocket.getLocalAddress() + " " + receivingSocket.getPort() + " " + receivingSocket .getLocalPort());
				Thread.sleep(1000);									//DEBUG ONLY, emulate network latency
				
				System.out.println("Received");
				input = bufferedReaderToString( new BufferedReader(new InputStreamReader(receivingSocket.getInputStream())));
				flag = Character.getNumericValue(input.charAt(0));
			}catch(IOException | InterruptedException e){ e.printStackTrace();}
			
			try
			{
				ArrayList<MessageSender> responses = new ArrayList<>();
				
				switch (flag) {
					case 1:
						responses = x.executeOperation(new LoginOperation(input, receivingSocket.getInetAddress()));
						break;
					case 2:
						responses = x.executeOperation(new RegistrationOperation(input, receivingSocket.getInetAddress()));
						break;
					case 3:
						responses = x.executeOperation(new ReceiveMessageOperation(input, receivingSocket.getInetAddress()));
						break;
					default:
						System.out.println("What is " + flag);
				}
				for (MessageSender ms : responses) {
					ms.send();
				}
			} catch(Exception e)
			{
				e.printStackTrace();
				new MessageSender(receivingSocket.getInetAddress(), SessionKeeper.INTERNAL_ERROR).send();
			}
		}
	}
	private static String bufferedReaderToString(BufferedReader br) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int c;
		while((c=br.read()) != -1)
		{
			sb.append((char)c);
		}
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		Server server = new Server();
		server.listen();
	}
}