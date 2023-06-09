import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class reciver implements Runnable{

    SocketChannel socket;
    public reciver(SocketChannel socket)
    {
        this.socket=socket;
    }
    @Override
    public void run() {
        try{
            while(true)
            {
                ByteBuffer buffer = ByteBuffer.allocate(256);
                socket.read(buffer);
                buffer.position();
                byte[] bytes = buffer.array();
                bytes[buffer.position()]='\0';
                System.out.println(new String(bytes,StandardCharsets.UTF_8).trim());

            }
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }
}
