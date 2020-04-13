package chat;


import java.net.InetAddress;
import java.util.ArrayList;

public class LoginOperation implements ClientInteraction
{
	String userInput;
	InetAddress from;
	
	public LoginOperation(String userInput, InetAddress from)
	{
		this.userInput = userInput;
		this.from = from;
	}
	
	@Override
	public ArrayList<MessageSender> execute()
	{
		return SessionKeeper.INSTANCE.login(userInput, from);
	}
	
	
	
}
