package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Anthony on 5/3/2016.
 */
public class Observer extends LeagueData{
    public String encryptionKey;

    public static Observer getObserver(JSONObject data){
        Observer observer = null;
        try{
            JSONObject object = data.getJSONObject("observer");
            observer = new Observer(object);

        } catch (Exception ex){
        } finally {
            return observer;
        }
    }

    public Observer (JSONObject data){
        //Get encryptionKey from data
        encryptionKey = getString(data, "encryptionKey");
    }
}
