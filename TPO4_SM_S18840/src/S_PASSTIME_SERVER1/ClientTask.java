/**
 *
 *  @author Sroczyński Mikołaj S18840
 *
 */

package S_PASSTIME_SERVER1;

import java.util.concurrent.FutureTask;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientTask extends FutureTask<String> {
    public Client client = new Client();
    static StringBuffer stringBufferLog = new StringBuffer();


    public ClientTask(Callable<String> stringCallable) {

        super(stringCallable);

    }
    public Client append(){
        return client ;
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes){
        return new ClientTask(()->{
            //c.send("login " + c.id);
            //do poprawy
            c.connect();
            c.send("login " + c.id);
            //c.send("login and id"+c.host +" "+c.id);
            reqs.forEach((string)->{
                String sent = c.send(string);
                if(showSendRes)
                    System.out.println(sent);
                //try {
                //    c.wait();
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
            });

            stringBufferLog.append(c.send("bye and log transfer"));
            //c.wait();
            //System.out.println(log.toString());
            return stringBufferLog.toString();

        });
    }
}