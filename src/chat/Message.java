package chat;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Message
{
    int flag;
    String content;
    InetAddress destinationAddress = null;
    
    public Message(int flag, String content)
    {
        this.flag = flag;
        this.content = content;
    }
    public Message(int flag, String content, InetAddress destinationAddress)
    {
        this.flag = flag;
        this.content = content;
        this.destinationAddress = destinationAddress;
    }
}
