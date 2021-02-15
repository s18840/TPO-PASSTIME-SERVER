/**
 *
 *  @author Sroczyński Mikołaj S18840
 *
 */

package S_PASSTIME_SERVER1;

import java.io.BufferedReader;
import org.yaml.snakeyaml.Yaml;
import java.util.Map;
import java.io.FileReader;

public class Tools {
    public static Options createOptionsFromYaml(String file) throws Exception{
        try {
            Yaml yaml = new Yaml();
            String host ="host";
            String port ="port";
            String concurMode="concurMode";
            String showSendRes ="showSendRes";
            String clientsMap ="clientsMap";
            BufferedReader bR = new BufferedReader(new FileReader(file));
            String str="";
            String strNr;
            while((strNr=bR.readLine())!=null) {
                str = str + strNr + '\n';
            }
            Map map = yaml.load(str);
            bR.close();
            return new Options(map.get(host).toString(),(int)map.get(port),(boolean)map.get(concurMode),(boolean)map.get(showSendRes),(Map)map.get(clientsMap));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

