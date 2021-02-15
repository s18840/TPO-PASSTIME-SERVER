/**
 *
 *  @author Sroczyński Mikołaj S18840
 *
 */

package S_PASSTIME_SERVER1;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.SocketChannel;
import java.nio.CharBuffer;
import java.net.InetSocketAddress;

public class Client{

    SocketChannel socketClient;
    public String id;
    public String host;
    public String resp;
    public int port;

    public Client() {
    }

    public final Charset charsetUtf8 = StandardCharsets.UTF_8;

    public Client(String host, int port, String id){
        this.id=id;
        this.port=port;
        this.host=host;
    }
    public String send(String req){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(req.getBytes().length);
        try {
            //byteBuffer.clear();
            byteBuffer.put(charsetUtf8.encode(req));

            byteBuffer.flip();
            //byteBuffer.clear();
            socketClient.write(byteBuffer);

        } catch (IOException e) {

        }
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(1024);
        StringBuilder sBResponser = new StringBuilder();
        try {
            int reder;
            while(true) {
                if ((reder = socketClient.read(byteBuffer1)) >= 1)
                    break;
            }
            while( reder > 0){
                reder = socketClient.read(byteBuffer1);
                byteBuffer1.flip();
                CharBuffer charBuffer = charsetUtf8.decode(byteBuffer1);
                //sBResponser.insert(port,host);
                sBResponser.append(charBuffer);
            }

        }catch (IOException e){
            System.out.println("Cant send a response");
        }
        //System.out.println(sBResponser.toString());
        return sBResponser.toString();
    }
    public void connect(){
        try {
            socketClient = SocketChannel.open();
            //socketClient.getLocalAddress();
            socketClient.configureBlocking(false);
            socketClient.connect(new InetSocketAddress(host,port));
            while (true) {
                if (socketClient.finishConnect())
                    //socketClient.close();
                    break;
            }
        }catch(IOException e){
            System.out.println("Not created socket");
        }
    }
    
}

