package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static lyle.cse216.lehigh.edu.tutorialforlyle.MainActivity.getUsernameById;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    String url = "https://sleepy-dusk-34987.herokuapp.com/";
//    private int netVotes;

    class ViewHolder extends RecyclerView.ViewHolder {
        Button like;
        Button dislike;
        TextView mTitle;
        TextView mBody;
        TextView mVotes;
        TextView username;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mBody = (TextView) itemView.findViewById(R.id.listItemText);
            this.mVotes = (TextView) itemView.findViewById(R.id.listItemVotes);

            this.like = (Button) itemView.findViewById(R.id.likeButton);
            this.dislike = (Button) itemView.findViewById(R.id.dislikeButton);

            this.username = (TextView) itemView.findViewById(R.id.viewUsername);
        }

    }


    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> mData;
    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.UserInfo> uInfo;
    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Votes> mVotes;
    private LayoutInflater mLayoutInflater;

    int uId;
    int mId;


    ItemListAdapter(Context context, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> data, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.UserInfo> users, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Votes> votes) {
        mData = data;
        uInfo = users;
        mVotes = votes;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final lyle.cse216.lehigh.edu.tutorialforlyle.Datum d = mData.get(position);
        holder.mTitle.setText(d.mTitle);
        holder.mBody.setText(d.mMessage);

        uId = d.user_id;
        mId = d.message_id;

        holder.username.setText("By " + getUsernameById(uInfo, uId));

//        holder.mVotes.setText();
        //get votes by message id

  //      netVotes = 0;

//        getUpVotes();
//        getDownVotes();
//        Log.d("YO", "VOTES HERE: " + netVotes);
//        String votes = Integer.toString(netVotes);


//        holder.mVotes.setText(votes); //can only pass String


        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };

        holder.mBody.setOnClickListener(listener);
        holder.mTitle.setOnClickListener(listener);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voteInfo;
                voteInfo = "upvotes/" + uId + "/" + mId; //userid/messageid
                sendPutRoute(voteInfo);
            }
        });

//        final View.OnClickListener dislikeButton = new View.OnClickListener() {
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voteType;
                voteType = "downvotes/" + uId + "/" + mId; //userid/messageid
                sendPutRoute(voteType);
            }

        });
    }

    /**
     * Helper method for GET function
     *
     * @param response string of JSON data obtained from Get
     */
    int getVoteCount(String response) {
        int len = -999;
        try {
            Log.d("lyle", "Getting netVotes " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("mData");
            len = json.length();
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
        }
        Log.d("lyle", "Successfully parsed JSON file.");
        return len;
    }

    void sendPutRoute(String voteInfo){
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url + voteInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lyle", response);
                //get value from response and place that value in the netVotes spot
//                holder.mVotes.setText(
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That PUT didn't work");
            }
        });
        MySingleton.getInstance(MySingleton.getContext().getApplicationContext()).addToRequestQueue(putRequest);

    }

    void sendDeleteRoute(int index){
        StringRequest putRequest = new StringRequest(Request.Method.DELETE, url + index, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lyle", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That PUT didn't work");
            }
        });
        Context context = MySingleton.getContext();
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(putRequest);
    }



//    void getUpVotes(){
//        Log.d("lyle", "MID: " + mId);
//        StringRequest upVotesRequest = new StringRequest(Request.Method.GET, url + "messages/upvotes/" + mId, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                netVotes = netVotes + getVoteCount(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("lyle", "That GET didn't work");
//            }
//        });
//    MySingleton.getInstance(MySingleton.getContext()).addToRequestQueue(upVotesRequest);
//    }
//
//    void getDownVotes(){
//        StringRequest downVotesRequest = new StringRequest(Request.Method.GET, url + "messages/downvotes/" + mId, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("lyle", response);
//                int downVotes = getVoteCount(response);
//                netVotes = netVotes - downVotes;
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("lyle", "That GET didn't work");
//            }
//        });
//        MySingleton.getInstance(MySingleton.getContext()).addToRequestQueue(downVotesRequest);
//    }

    interface ClickListener{
        void onClick(Datum d);
    }
    private ClickListener mClickListener;

    ClickListener getClickListener() {
        return mClickListener;
    }

    void setClickListener(ClickListener c) {
        mClickListener = c;
    }


}