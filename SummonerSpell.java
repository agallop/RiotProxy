package hi.apitest;

import android.provider.ContactsContract;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/8/2016.
 */
public class SummonerSpell extends LeagueData{
    public final Image image;
    public final Long cooldown;

    public SummonerSpell(JSONObject data){
        image = Image.getImage(data);
        long cooldown = 0;
        try {
            cooldown = data.getJSONArray("cooldown").getLong(0);
        }catch (Exception ex){
            cooldown = 0;
        }
        finally {
            this.cooldown = cooldown;
        }
    }
}
