package chat;

import java.net.InetAddress;
import java.util.ArrayList;

@FunctionalInterface
public interface ClientInteraction
{
	ArrayList<MessageSender> execute();			//return string is the message to be sent back to the
}

