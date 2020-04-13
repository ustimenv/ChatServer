package chat;

import java.net.InetAddress;
import java.util.Objects;

public class User
{
    String username;
    int passwordHash;
    InetAddress address;
    
    public User(String username, int passwordHash)
    {
       this.username = username;
       this.passwordHash = passwordHash;
    }
    
    public User(String username, int passwordHash, InetAddress address)
    {
        this.username = username;
        this.passwordHash = passwordHash;
        this.address = address;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
