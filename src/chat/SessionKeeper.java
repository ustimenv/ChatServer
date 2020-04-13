package chat;
import java.net.InetAddress;
import java.util.*;

public enum SessionKeeper
{
    INSTANCE;
	
    //Outgoing flags
    static char INTERNAL_ERROR = '0';
	static char LOGIN_ACK = '1';
	static char LOGIN_NAK = '2';
	static char REGISTRATION_ACK = '3';
	static char REGISTRATION_NAK = '4';
	
	
	//Incoming flags
	static char LOGIN_ASK = '5';
	static char REGISTRATION_ASK = '6';
	static char MESSAGE_FLAG = '7';
	
	//Bidirectional flags
	static String DELIMITER = "#";

	private Set <User> users = new HashSet<>();
 	
	
    public ArrayList<MessageSender> login(String input, InetAddress from)
	{
        StringTokenizer st = new StringTokenizer(input, DELIMITER);
        String flag = st.nextToken();
        String username = st.nextToken();
        String password = st.nextToken();
		ArrayList <MessageSender> response = new ArrayList<>();
		
		int passwordHash = password.hashCode();
		
        for(User user : users)
        {
            if(user.username.equals(username) && passwordHash == user.passwordHash)
            {
				System.out.println("Valid credentials!");
				response.add(new MessageSender(from, SessionKeeper.LOGIN_ACK));
				return response;
            }
		}
		System.out.println("Invalid credentials!");
		response.add(new MessageSender(from, SessionKeeper.LOGIN_NAK));
        return response;
    }
    
    public ArrayList <MessageSender> register(String input, InetAddress from)
    {
    	ArrayList <MessageSender> response = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(input, DELIMITER);
        String flag = st.nextToken();
        String username = st.nextToken();
        String password = st.nextToken();
        
        User potentialUser = new User(username, password.hashCode(), from);
        
        System.out.println("Registering " +" " + username + " " + password);
        //TODO write own hash function
		
        int assignedID = users.size();
        if(users.contains(potentialUser))
        {
            System.out.println("Username " + username + " taken");
            response.add(new MessageSender(from, SessionKeeper.REGISTRATION_NAK));
   
        } else
        {
            users.add(potentialUser);
            System.out.println("Successfully registered " + username);
            response.add(new MessageSender(from, SessionKeeper.REGISTRATION_ACK));
        }
        return response;
    }

    public ArrayList <MessageSender> forwardTweet(String input, InetAddress from)
    {
		StringTokenizer st = new StringTokenizer(input, DELIMITER);
		String flag = st.nextToken();
		String contents = st.nextToken();
		System.out.println("Received message " + contents);
	
		ArrayList <MessageSender> messages = new ArrayList<>();
        for(User user : users)
		{
			messages.add(new MessageSender(user.address, SessionKeeper.MESSAGE_FLAG+"", contents));
		}
        
        return messages;
    }
}