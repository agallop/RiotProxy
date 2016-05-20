package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/9/2016.
 */
public class Champion extends LeagueData {
    public final Image image;
    public final String name;

    public Champion(JSONObject data){
        image = Image.getImage(data);
        name = getString(data, "name");
    }
}
