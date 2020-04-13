package chat;

import java.util.ArrayList;

public class ClientOperationsExecutor
{

	ArrayList<MessageSender> executeOperation(ClientInteraction clientInteraction)
	{
		return clientInteraction.execute();
	}
}
