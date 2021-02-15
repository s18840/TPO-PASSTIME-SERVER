/**
 *
 *  @author Sroczyński Mikołaj S18840
 *
 */

package S_PASSTIME_SERVER1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;


public class Server {
    String host;
    int port;
    private ServerSocketChannel serverSocketChannel;
    SelectionKey sKey;
    boolean flague;
    Map<SocketChannel,String> clientNames;
    Map<SocketChannel,String> clientLogs;
    private Selector selector = null;
    StringBuilder log;
    volatile boolean serverStatus;
    Server(String host,int port) throws IOException{
        this.host = host;
        this.port = port;
        clientNames = new HashMap<>();
        clientLogs = new HashMap<>();
        log = new StringBuilder();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(host,port));

        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        //selector= SelectableChannel;
        sKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        log = new StringBuilder();


    }
    public void startServer(){

        new Thread(()-> {
            serverStatus = true;

            while(serverStatus) {
                try {

                    selector.select();

                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                    Set<SelectionKey> keysSelection = selector.selectedKeys();

                    Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();

                    while (selectionKeyIterator.hasNext()){

                        SelectionKey key = selectionKeyIterator.next();
                        selectionKeyIterator.remove();

                        if(key.isAcceptable()){

                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            continue;
                        }
                        if(key.isReadable()){
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            serviceRequest(clientChannel);
                        }

                    }


                }catch (IOException e){}

            }

        }).start();

    }

    private static Charset charset = StandardCharsets.UTF_8;


    public void stopServer() {

        serverStatus = false;

    }


    public String getServerLog() {

        return log.toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public StringBuilder getLog() {
        return log;
    }

    public void setLog(StringBuilder log) {
        this.log = log;
    }

    public boolean isServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(boolean serverStatus) {
        this.serverStatus = serverStatus;
    }

    private void serviceRequest(SocketChannel socketChannel) {

        if (!socketChannel.isOpen()) return;
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.clear();

        try {
            for (int bytesRead = socketChannel.read(byteBuffer); bytesRead > 0; bytesRead = socketChannel.read(byteBuffer)) {
                byteBuffer.flip();
                //byteBuffer.clear();
                CharBuffer charBuffer = charset.decode(byteBuffer);
                str.append(charBuffer);
            }
            handleRequest(socketChannel, str.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(SocketChannel socketChannel,String request) throws IOException {

        StringBuilder response = new StringBuilder();
        String[] array = new String[4];
        array[3] = "";
//        String req ="";
//        switch (request){
//            case "login":
//
//                String str = "logged in";
//                response.append(str);
//                clientNames.put(sc, request.substring(request.indexOf(' ')+1));
//                log.append(clientNames.get(sc)+" logged in at "+ LocalTime.now()+'\n');
//                clientLogs.put(sc,"=== "+clientNames.get(sc)+ " log start ==="+'\n'+"logged in"+'\n');
//                break;
//            case"bye":
//                String str1 = "logged out";
//                response.append(str1);
//                log.append(clientNames.get(sc)+" logged out at "+ LocalTime.now()+'\n');
//                clientLogs.put(sc,clientLogs.get(sc)+str1+'\n'+"=== "+clientNames.get(sc)+ " log end ==="+'\n');
//                break;
//            case "bye and log transfer":
//                String str2 = "logged out";
//                clientLogs.put(sc,clientLogs.get(sc)+str2+'\n'+"=== "+clientNames.get(sc)+ " log end ==="+'\n');
//                log.append(clientNames.get(sc)+" logged out at "+ LocalTime.now()+'\n');
//
//                response.append(clientLogs.get(sc));
//                default:
//                    String result = Time.passed(request.substring(0, request.indexOf(' ')), request.substring(request.indexOf(' ')+1));
//                    response.append(result);
//                    log.append(clientNames.get(sc)+" request at "+ LocalTime.now()+": \""+ request+"\""+'\n');
//                    clientLogs.put(sc,clientLogs.get(sc)+"Request: "+ request+'\n'+"Result: "+'\n'+result+'\n');
//break;
//        }

        if(request.contains("login")){

            String str = "logged in";
            response.append(str);
            clientNames.put(socketChannel, request.substring(request.indexOf(' ')+1));
            log.append(clientNames.get(socketChannel)+" logged in at "+ LocalTime.now()+'\n');
            clientLogs.put(socketChannel,"=== "+clientNames.get(socketChannel)+ " log start ==="+'\n'+"logged in"+'\n');

        }else if(request.equals("bye")){

            String str = "logged out";
            response.append(str);
            log.append(clientNames.get(socketChannel)+" logged out at "+ LocalTime.now()+'\n');
            clientLogs.put(socketChannel,clientLogs.get(socketChannel)+str+'\n'+"=== "+clientNames.get(socketChannel)+ " log end ==="+'\n');

        }else if(request.equals("bye and log transfer")){

            String str = "logged out";
            clientLogs.put(socketChannel,clientLogs.get(socketChannel)+str+'\n'+"=== "+clientNames.get(socketChannel)+ " log end ==="+'\n');
            log.append(clientNames.get(socketChannel)+" logged out at "+ LocalTime.now()+'\n');

            response.append(clientLogs.get(socketChannel));

        }else{

            String result = Time.passed(request.substring(0, request.indexOf(' ')), request.substring(request.indexOf(' ')+1));
            response.append(result);
            log.append(clientNames.get(socketChannel)+" request at "+ LocalTime.now()+": \""+ request+"\""+'\n');
            clientLogs.put(socketChannel,clientLogs.get(socketChannel)+"Request: "+ request+'\n'+"Result: "+'\n'+result+'\n');
        }


        ByteBuffer out = ByteBuffer.allocateDirect(response.toString().getBytes().length);

        out.put(charset.encode(response.toString()));
        out.flip();
        socketChannel.write(out);

    }
}
