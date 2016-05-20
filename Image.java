package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/9/2016.
 */
public class Image extends LeagueData {
    public final String full;

    public static Image getImage(JSONObject data){
        Image image = null;
        try{
            JSONObject object = data.getJSONObject("image");
            image = new Image(object);

        } catch (Exception ex){
        } finally {
            return image;
        }
    }

    public Image(JSONObject data){
        full = getString(data, "full");
    }
}
