package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Anthony on 4/30/2016.
 */
public class MasteryPages extends LeagueData{
    final Set<MasteryPage> pages;
    final long summonerId;



    public MasteryPages(JSONObject data) {

        //Getting Mastert Pages from data
        pages = MasteryPage.getPages(data);

        summonerId = getLong(data, "summonerId");

    }

    public int count() {
        return pages.size();
    }

    public Iterator<MasteryPage> iterator(){
        return pages.iterator();
    }

}


