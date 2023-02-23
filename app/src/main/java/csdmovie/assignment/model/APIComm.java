/**
 * Author:    Ahamed Careem
 **/
package csdmovie.assignment.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class APIComm {
    
    private Context context;
    private static APIComm instance;
    private RequestQueue queue;
    private static String[] output;
    private String response;
    private static String tag = "DEBUG PROGRESS: ";
    
    //Contructor to set up certain things

    /**
     * This is a private constructor to maintain the singleton instance
     * @param context
     */
    private APIComm (Context context){
        this.context = context.getApplicationContext();
        queue = Volley.newRequestQueue(context);
        Log.d(tag, "APIComm constructed!");
    }


    /*public static Response.Listener<JSONObject> searchListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject resp) {

            //Now to parse the information
            JSONObject obj1 = null;
            try {
                obj1 = resp.getJSONObject("RestResponse");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            assert obj1 != null;
            //From that object, we extract the array of actual data labeled result
            JSONArray array = obj1.optJSONArray("result");
            ArrayList<State> states = new ArrayList<>();
            for(int i=0; i < array.length(); i++) {

                try {
                    //for each array element, we have to create an object
                    JSONObject jsonObject = array.getJSONObject(i);
                    State s = new State();
                    assert jsonObject != null;
                    s.setName(jsonObject.optString("name"));
                    s.setA2Code(jsonObject.optString("alpha2_code"));
                    s.setA3Code(jsonObject.optString("alpha3_code"));
                    //save the object for later
                    states.add(s);


                } catch (JSONException e) {
                    Log.d("VolleyApp", "Failed to get JSON object");
                    e.printStackTrace();
                }
            }

        }
    };

    public static Response.Listener<JSONObject> titleListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject resp) {
            Log.d(tag, "Started Class titleListener");

            output[0] = "Movie 1";
            output[1] = "Movie 2";
            output[2] = "Movie 3";
            output[3] = "Movie 4";
            output[4] = "Movie 5";
            output[5] = "Movie 6";
            output[6] = "Movie 7";
            output[7] = "Movie 8";
            output[8] = "Movie 9";
            output[9] = "Movie 10";
            Log.d(tag, "Edited the answer array in MovieManager");



        }
    };*/

    /*public void pullFromURL(String url, Response.Listener<JSONObject> listener, String[] output) {

        APIComm.output = output;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, listener, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        response = "Bad Response";
                    }
                });
        //this actually queues up the async response with Volley
        queue.add(jsObjRequest);
    }*/
    
    
    //These are our methods for getting the singleton instances

    /**
     * This method returns the APIComm with the instance provided
     * @param context the current context
     * @return
     */
    public static APIComm getInstance(Context context) {
        if (instance == null) {
            instance = new APIComm(context);
        }
        Log.d(tag, "Instance of APIComm created.");
        return instance;
    }

    /**
     * This method returns the instance without the context (attempts)
     * @return
     */
    public static APIComm getInstance() {
        if (instance == null) {
            throw new RuntimeException("Singleton has not been initialized.\n"
                    + "Please call with a context first.");
        }
        Log.d(tag, "Instance of APIComm retrieved.");
        return instance;
    }

    /**
     * This methos adds a request to the queue
     * @param a the request needing to be added
     */
    public static void add(Request a) {
        instance.queue.add(a);
    }
}
