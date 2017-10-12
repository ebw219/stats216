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

import static lyle.cse216.lehigh.edu.tutorialforlyle.MainActivity.adapter;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    String url = "https://sleepy-dusk-34987.herokuapp.com/";

    class ViewHolder extends RecyclerView.ViewHolder {
        Button like;
        Button dislike;
        TextView mTitle;
        TextView mBody;
        TextView mVotes;

//        Button delete;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mBody = (TextView) itemView.findViewById(R.id.listItemText);
            this.mVotes = (TextView) itemView.findViewById(R.id.listItemVotes);

            this.like = (Button) itemView.findViewById(R.id.likeButton);
            this.dislike = (Button) itemView.findViewById(R.id.dislikeButton);


//            this.delete = (Button) itemView.findViewById(R.id.deleteButton);
        }

    }


    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> mData;
    private LayoutInflater mLayoutInflater;

    int uId;
    int mId;

    ItemListAdapter(Context context, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> data) {
        mData = data;
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


//    FloatingActionButton newMessage = (FloatingActionButton) findViewById(R.id.add);
//        newMessage.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent input = new Intent(getApplicationContext(), NewMessageActivity.class);
//            input.putExtra("label_contents", "Add new message");
//            startActivityForResult(input, 789);
//        }
//
//    });

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final lyle.cse216.lehigh.edu.tutorialforlyle.Datum d = mData.get(position);
        holder.mTitle.setText(d.mTitle);
        holder.mBody.setText(d.mMessage);
        holder.mVotes.setText(d.mVotes + ""); //can only pass String

        uId = d.user_id;
        mId = d.message_id;

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

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("lyle", "DELETE ID: " + d.mIndex);
//                StringRequest getRequest = new StringRequest(Request.Method.DELETE, url + "/" + d.mIndex, new Response.Listener<String>(){
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("lyle", response);
//                    }
//                }, new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("lyle", "That delete didn't work");
//                    }
//                });
//                Context context = MySingleton.getContext();
//                MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(getRequest);
//            }
//        });


    }

//    void sendGetRoute(int index){
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url + index, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response) {
//                Log.d("lyle", response);
//            }
//        }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("lyle", "That get didn't work");
//            }
//        });
//        Context context = MySingleton.getContext();
//        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(getRequest);
//    }

    void sendPutRoute(String voteInfo){
        Log.d("lyle", "HERE");
        //use index to find the right textview and change value/contents

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url + voteInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lyle", response);
                //get value from response and place that value in the votes spot
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