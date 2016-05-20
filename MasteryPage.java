package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Anthony on 4/30/2016.
 */
public class MasteryPage extends LeagueData{
    public final boolean current;
    public final long id;
    public final List<Mastery> masteries;
    public final String name;

    /* Returns a Set of MasteryPage objects given a JSONObject */
    public static Set<MasteryPage> getPages(JSONObject data){
        TreeSet<MasteryPage> pages = new TreeSet<MasteryPage>();
        try {
            JSONArray dataArray = data.getJSONArray("pages");
            int size = dataArray.length();
            for (int i = 0; i < size; i++) {
                pages.add(new MasteryPage(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex) {
        } finally {
            return pages;
        }
    }

    public MasteryPage(JSONObject data){

        //Getting current from data
        current = getBoolean(data, "current");

        //Getting id from data
        id = getLong(data, "id");

        //Getting masteries from data
        masteries = Mastery.getMasteries(data);

        //Getting name from data
        name = getString(data, "name");

    }

    /* iterator */
    public Iterator<Mastery> iterator(){
        return masteries.iterator();
    }
}
