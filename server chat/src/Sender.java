import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Sender implements Runnable{

    SocketChannel socket;
    List<ByteBuffer> listOfMesseges;
    public Sender(SocketChannel socket, List<ByteBuffer> listOfMesseges)
    {
        this.socket=socket;
        this.listOfMesseges=listOfMesseges;
    }

    @Override
    public void run() {
        try {

        while(true)
        {
            synchronized (listOfMesseges) {
                if(!listOfMesseges.isEmpty()) {
                    ByteBuffer buffer = listOfMesseges.remove(0);
                    buffer.flip();
                    socket.write(buffer);
                    buffer.clear();
                }else
                {
                    listOfMesseges.wait(1000);
                }
            }
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
