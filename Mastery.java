package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 4/30/2016.
 */
public class Mastery extends LeagueData{
    public final int id;
    public final int rank;

    /* Returns a List of Mastery objects given a JSONObject */
    public static List<Mastery> getMasteries(JSONObject data){
        List<Mastery> masteries = new ArrayList<Mastery>();
        try{
            JSONArray dataArray = data.getJSONArray("masteries");
            int size = dataArray.length();
            for(int i = 0; i < size; i++){
                masteries.add(new Mastery(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex){
        } finally {
            return masteries;
        }
    }

    //Creats a Mastery using primitives
    public Mastery(int id, int rank){
        this.id = id;
        this.rank = rank;
    }


    // Creates a Mastery using a JSON
    public Mastery(JSONObject data){
        //Get id from data
        id = getInt(data, "id");

        //get rank from data
        rank = getInt(data, "rank");
    }

    /* Returns true if the id is the same */
    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(!(obj instanceof Mastery))
            return false;
        if(((Mastery) obj).id == this.id)
            return true;
        return false;
    }


}
