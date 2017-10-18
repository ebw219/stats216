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

import java.util.ArrayList;

import static lyle.cse216.lehigh.edu.tutorialforlyle.MainActivity.getUsernameById;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    String url = "https://sleepy-dusk-34987.herokuapp.com/";


    private static int totVotes;

    private static void resetTotVotes(){
        totVotes = 0;
    }

    private static void addTotVotes(int i){
        totVotes += i;
    }

    private static String getTotVotes() {
        return totVotes + "";
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button like;
        Button dislike;
        TextView mTitle;
        TextView mBody;
        TextView mVotes;
        TextView username;

        TextView mid_spot;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mBody = (TextView) itemView.findViewById(R.id.listItemText);
            this.mVotes = (TextView) itemView.findViewById(R.id.listItemVotes);

            this.like = (Button) itemView.findViewById(R.id.likeButton);
            this.dislike = (Button) itemView.findViewById(R.id.dislikeButton);

            this.username = (TextView) itemView.findViewById(R.id.viewUsername);

            this.mid_spot = (TextView) itemView.findViewById(R.id.mid_spot);
        }

    }


    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> mData;
    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.UserInfo> uInfo;
    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Votes> mVotesList;
    private LayoutInflater mLayoutInflater;

    private int uId;
    private int mId;


    ItemListAdapter(Context context, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> data, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.UserInfo> users, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Votes> votes) {
        mData = data;
        uInfo = users;
        mVotesList = votes;
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

        holder.mid_spot.setText(mId + "");

        String byName = getUsernameById(uInfo, uId);

        Log.d("lyle", "USERNAME: " + byName);

        holder.username.setText("By " + byName);

        resetTotVotes();
        getVotes("upvotescount", mId, d);
        getVotes("downvotescount", mId, d);

        Log.i("lyle", "mvotes: " + d.getmVotes());
        holder.mVotes.setText(d.getmVotes() + "");
        //get votes by message id


        // Attach a click listener to the view we are configuring

        final View.OnClickListener listener = new View.OnClickListener() { //to view comments
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };

        //click to view comments
        holder.mBody.setOnClickListener(listener);
        holder.mTitle.setOnClickListener(listener);

        //click to like -- no visible reaction to click
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voteInfo;
                voteInfo = "upvotes/" + uId + "/" + mId; //userid/messageid
                sendPostRoute(voteInfo);
            }
        });


        //click to dislike
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voteType;
                voteType = "downvotes/" + uId + "/" + mId; //userid/messageid
                sendPostRoute(voteType);
            }

        });
    }

    //route to get votes
    private void getVotes(final String voteType, final int mId, final Datum d){
        MySingleton.getInstance(MySingleton.getContext().getApplicationContext()).addToRequestQueue(
                new StringRequest(Request.Method.GET, url + "messages/" + voteType + "/" + mId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", response);

                        switch(voteType){
                            case "upvotescount":
                                d.setmVotes(Integer.parseInt(getTotVotes()));
                                break;

                            case "downvotescount":
                                addTotVotes(-1*Integer.parseInt(response));
                                break;

                            default:
                                addTotVotes(0);
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("lyle", error.getMessage());
                    }
                })
        );
    }


    //route to send a vote request
    private void sendPostRoute(String voteInfo){
        StringRequest putRequest = new StringRequest(Request.Method.POST, url + voteInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lyle", response);
                //get value from response and place that value in the netVotes spot
//                holder.mVotesList.setText(
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That POST didn't work");
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
        MySingleton.getInstance(MySingleton.getContext().getApplicationContext()).addToRequestQueue(putRequest);
    }



    interface ClickListener{
        void onClick(Datum d);
    }
    private ClickListener mClickListener;

    void setClickListener(ClickListener c) {
        mClickListener = c;
    }


}