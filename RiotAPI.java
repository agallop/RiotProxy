package hi.apitest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

/**
 * Created by Anthony on 4/26/2016.
 */
public class RiotAPI {

    private static String KEY;
    private static final Semaphore resultSemaphore = new Semaphore(1);
    private static Object callResult;

    //Caching results from http connections
    private static Map<Long, Bitmap> summonersImageCache = new TreeMap<Long, Bitmap>();
    private static Map<Long, Bitmap> championImageCache = new TreeMap<Long, Bitmap>();
    private static Map<Long, Champion> championCache = new TreeMap<Long, Champion>();
    private static Map<Long, SummonerSpell> summonersCache = new TreeMap<Long, SummonerSpell>();
    private static Map<String, Summoner> summonerCache = new TreeMap<String, Summoner>();

    //Current version for lol static data
    private static String currentVersion;

    /* Sets the API key
    * Required before calling any other method */
    public static void setKey(String key) {
        KEY = key;
        currentVersion = getCurrentVersion();
    }

    // Gets simplified summonerName. Lowercase characters with no spaces
    private static String toSimple(String string){
        StringBuilder builder = new StringBuilder();
        for(char c : string.toCharArray()){
            char cur = Character.toLowerCase(c);
            if(cur <= 'z' && cur >= 'a'){
                builder.append(c);
            }
        }
        return builder.toString();
    }
    public static TreeMap<String, Summoner> getSummonerByName(String region, final List<String> summonerNames) {

        //Holds the result of the call
        TreeMap<String, Summoner> result = new TreeMap<String, Summoner>();
        //Building string for API call
        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/api/lol/");
        builder.append(region);
        builder.append("/v1.4/summoner/by-name/");
        int i;

        //Used to determine if the request information is already cached
        boolean cached = true;

        //Adds each name to string, if it is not already cached
        for(i = 0; i < summonerNames.size() - 1; i++){
            String simpleName = toSimple(summonerNames.get(i));
            if(!summonerCache.containsKey(simpleName)) {
                builder.append(simpleName);
                builder.append(",%20");
                cached = false;
            } else {
                result.put(summonerNames.get(i), summonerCache.get(simpleName));
            }
        }

        //Adds last name without trailing space
        String simpleName = toSimple(summonerNames.get(i));
        if(!summonerCache.containsKey(simpleName)) {
            builder.append(simpleName);
            cached = false;
        } else {
            result.put(summonerNames.get(i), summonerCache.get(simpleName));
        }
        builder.append("?api_key=");
        builder.append(KEY);

        final String query = builder.toString();

        // Makes API call if needed
        if(!cached) {
            // Pasrsing Json Map
            JSONObject jsonResult = getJsonObject(query);
            Iterator<String> iterator = summonerNames.iterator();

            //Adds and caches Summoner objects from API call
            while (iterator.hasNext()) {
                try {
                    String name = toSimple(iterator.next());
                    simpleName = toSimple(name);
                    if(jsonResult.has(simpleName)) {
                        summonerCache.put(simpleName, new Summoner(jsonResult.getJSONObject(simpleName)));
                        result.put(name, summonerCache.get(simpleName));

                    }
                } catch (Exception ex) {

                }
            }
        }

        return result;
    }

    public static TreeMap<String, MasteryPages> getMasteries(final List<String> summonerIds, String region) {
        if(summonerIds == null)
            throw new IllegalArgumentException();
        if(summonerIds.size() == 0)
            throw new IllegalArgumentException();
        //Building string for query
        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/api/lol/");
        builder.append(region);
        builder.append("/v1.4/summoner/");
        int i;
        for(i = 0; i < summonerIds.size() - 1; i++){
            builder.append(summonerIds);
            builder.append(",%20");
        }
        builder.append(summonerIds.get(i));
        builder.append("/masteries?api_key=");
        builder.append(KEY);

        final String query = builder.toString();

        TreeMap<String, MasteryPages> result = new TreeMap<String, MasteryPages>();

        //Parsing Json into Map
        JSONObject jsonResult = getJsonObject(query);
        Iterator<String> iterator = summonerIds.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            try {
                JSONObject masteryPagesDto = jsonResult.getJSONObject(id);
                result.put(id, new MasteryPages(masteryPagesDto));
            }
            catch (Exception ex){}
        }
        return result;
    }

    public CurrentGameInfo getCurrentGame(long summonerId, String region){

        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/");
        builder.append(region);
        builder.append("/");
        builder.append(summonerId);
        builder.append("?api_key=");
        builder.append(KEY);

        final String query = builder.toString();
        return new CurrentGameInfo(getJsonObject(query));
    }

    public static String getCurrentVersion(){


        StringBuilder builder = new StringBuilder();
        builder.append("https://global.api.pvp.net/api/lol/static-data/na/v1.2/versions?api_key=");
        builder.append(KEY);

        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting Thread, getCurrentVersion");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        if(response.first == 200) {
                            JSONArray jsonResult = new JSONArray(response.second);
                            resultSemaphore.acquire();
                            callResult = jsonResult.get(0);
                        }else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        String result;
        while (true) {
            try {
                latch.await();
                result = (String) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }



    public static Pair<Integer, String> httpGet(String urlStr) throws IOException {
        Log.d("MAIN", "httpstart");
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        HttpURLConnection conn = (HttpURLConnection) con;

        //Error
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            Log.d("MAIN", "response != 200");
            return new Pair<Integer, String>(responseCode , conn.getResponseMessage());
        }

        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            Log.d("MAIN", "line append");
            sb.append(line);
        }
        rd.close();
        Log.d("MAIN", "finish append");
        conn.disconnect();
        return new Pair<Integer, String>(responseCode, sb.toString());
    }

    public static Bitmap getSummonerImage(final Long id){
        if(!summonersImageCache.containsKey(id)){
            // Gets the
            SummonerSpell spell = getSummonerSpell(id);
            StringBuilder builder = new StringBuilder();
            builder.append("http://ddragon.leagueoflegends.com/cdn/");
            builder.append(currentVersion);
            builder.append("/img/spell/");
            builder.append(spell.image.full);
            final String query = builder.toString();
            final CountDownLatch latch = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        summonersImageCache.put(id, bitmapFromUrl(query));
                    } catch (Exception ex){
                        Log.d("getSummonerImage", ex.getMessage());
                    }
                    latch.countDown();
                }
            }).start();
            try {
                latch.await();
            }catch (Exception ex){

            }
        }
        return summonersImageCache.get(id);
    }

    public static Bitmap getChampionImage(final Long id){
        if(!championImageCache.containsKey(id)){
            Champion champion = getChampion(id);
            StringBuilder builder = new StringBuilder();
            builder.append("http://ddragon.leagueoflegends.com/cdn/");
            builder.append(currentVersion);
            builder.append("/img/champion/");
            builder.append(champion.image.full);
            final String query = builder.toString();
            final CountDownLatch latch = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                    championImageCache.put(id, bitmapFromUrl(query));
                        Log.d("getChampionImage", "champ" + id + " added tp cache");
                    } catch (Exception ex){
                        Log.d("getSummonerImage", ex.getMessage());
                    }
                    latch.countDown();
                }
            }).start();
            try {
                latch.await();
            }catch (Exception ex){

            }
        }
        return championImageCache.get(id);
    }

    public static SummonerSpell getSummonerSpell(Long id){
        if(summonersCache.containsKey(id)){
            return summonersCache.get(id);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("https://global.api.pvp.net/api/lol/static-data/na/v1.2/summoner-spell/");
        builder.append(id);
        builder.append("?spellData=all&api_key=");
        builder.append(KEY);

        final String query = builder.toString();
        summonersCache.put(id, new SummonerSpell(getJsonObject(query)));
        return summonersCache.get(id);
    }

    public static Champion getChampion(Long id){
        if(championCache.containsKey(id)){
            return championCache.get(id);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/");
        builder.append(id);
        builder.append("?champData=all&api_key=");
        builder.append(KEY);

        final String query = builder.toString();
        championCache.put(id, new Champion(getJsonObject(query)));
        return championCache.get(id);
    }

    //Returns a JSONObject given from a connection to query
    private static JSONObject getJsonObject(final String query){
        //To wait for result from connection
        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        if(response.first == 200) {
                            //Gain control of a global Object variable
                            resultSemaphore.acquire();
                            callResult = new JSONObject(response.second);
                        }else {
                            resultSemaphore.acquire();
                            callResult = null;
                        }
                        Log.d("getJsonObject", query + ": " + response.first);
                        break;
                    } catch (Exception ex) {
                        Log.d("getJsonObject", ex.getMessage());
                    }
                }
                latch.countDown();
            }
        }).start();

        //Waits for result from thread
        JSONObject result;
        while (true) {
            try {
                latch.await();
                result = (JSONObject) callResult;
                break;
            } catch (Exception ex) {
                Log.d("getJsonObject", ex.getMessage());
            }
        }

        //Release control of global Object variable
        resultSemaphore.release();
        return result;
    }

    //Gets a bitmap given the URL
    private static Bitmap bitmapFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);

        Log.d("RiotAPI", "getBitmapFromURL: " + url);
        return x;
    }

}
