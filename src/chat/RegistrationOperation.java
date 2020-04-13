package chat;


import java.net.InetAddress;
import java.util.ArrayList;

public class RegistrationOperation implements ClientInteraction
{
	String userInput;
	InetAddress from;
	
	public RegistrationOperation(String userInput, InetAddress from)
	{
		this.userInput = userInput;
		this.from = from;
	}
	
	@Override
	public ArrayList<MessageSender> execute()
	{
		return SessionKeeper.INSTANCE.register(userInput, from);
	}
	
}
