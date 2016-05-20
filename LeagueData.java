package hi.apitest;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Anthony on 5/3/2016.
 */
public abstract class LeagueData {
    public LeagueData() {
    }


    /* Gets int from JSONObject given a key. Returns 0 on default */
    protected int getInt(JSONObject data, String key) {
        //get int from Json
        int result = 0;
        try {
            result = data.getInt(key);
        } catch (Exception ex) {
        } finally {
            return result;
        }
    }

    /* Gets long from JSONObject given a key. Returns 0 on default */
    protected long getLong(JSONObject data, String key) {
        //get long from Json
        long result = 0;
        try {
            result = data.getLong(key);
        } catch (Exception ex) {
        } finally {
            return result;
        }
    }

    /* Gets long from JSONObject given a key. Returns null on default */
    protected String getString(JSONObject data, String key) {
        //get String from Json
        String result = null;
        try {
            result = data.getString(key);
        } catch (Exception ex) {
        } finally {
            return result;
        }
    }

    protected boolean getBoolean(JSONObject data, String key){
        //get Boolean from Json
        Boolean result = null;
        try {
            result = data.getBoolean(key);
        } catch (Exception ex) {
        } finally {
            return result;
        }
    }




}
