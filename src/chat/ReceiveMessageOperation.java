package chat;


import java.net.InetAddress;
import java.util.ArrayList;

public class ReceiveMessageOperation implements ClientInteraction
{
	String userInput;
	InetAddress from;
	
	public ReceiveMessageOperation(String userInput, InetAddress from)
	{
		this.userInput = userInput;
		this.from = from;
	}
	
	@Override
	public ArrayList<MessageSender> execute()
	{
		return SessionKeeper.INSTANCE.forwardTweet(userInput, from);
	}
}
