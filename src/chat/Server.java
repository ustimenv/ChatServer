package chat;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import com.sun.net.httpserver.*;
import javax.net.ssl.*;


public class Server
{
	SSLServerSocket serverSocket;
	SSLSocket receivingSocket;
	
//	ServerSocket serverSocket;
//	Socket receivingSocket;
	
	public Server()
	{
		try
		{
//			this.serverSocket = new ServerSocket(50000);
			System.setProperty("javax.net.debug", "ssl");
			
			KeyStore keyStore = KeyStore.getInstance("JKS");
			InputStream is = new BufferedInputStream(new FileInputStream("my_keystore"));
			keyStore.load(is, "123456".toCharArray());
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, "123456".toCharArray());
			
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(kmf.getKeyManagers(), null, null);
			
			SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) sslContext.getServerSocketFactory();
			
			this.serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(50000);
			this.serverSocket.setNeedClientAuth(false);
			this.serverSocket.setWantClientAuth(false);
		}catch(IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException e)
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
				receivingSocket = (SSLSocket) serverSocket.accept();;
				System.out.println(receivingSocket.getInetAddress() + " " + receivingSocket.getLocalAddress() + " " + receivingSocket.getPort() + " " + receivingSocket .getLocalPort());
				
				
//				for(String s : receivingSocket.getEnabledCipherSuites())
//					System.out.print(s + "\t");
//				System.out.println();
//				for(String s : serverSocket.getEnabledCipherSuites())
//					System.out.print(s + "\t");
				
				
				
				Thread.sleep(1000);									//DEBUG ONLY, emulate network latency

				System.out.println("Received");
				input = bufferedReaderToString( new BufferedReader(new InputStreamReader(receivingSocket.getInputStream())));
				flag = Character.getNumericValue(input.charAt(0));
				System.out.println(input);
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