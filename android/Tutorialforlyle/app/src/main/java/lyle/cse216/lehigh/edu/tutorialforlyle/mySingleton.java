package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MySingleton extends AppCompatActivity{

    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static Context getContext(){
        return mCtx;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

//        try {
//            Intent input = new Intent(getApplicationContext(), ItemListAdapter.class);
//            input.putExtra("label_contents", "Singleton");
//            startActivityForResult(input, 789);
//            setResult(Activity.RESULT_CANCELED);
//            finish();
//        } catch (RuntimeException e){
//            e.printStackTrace();
//        }

        return mRequestQueue;
    }

    public <Datum> void addToRequestQueue(Request<Datum> req) {
        getRequestQueue().add(req);
    }


}
