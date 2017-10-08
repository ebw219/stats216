package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    String url = "https://sleepy-dusk-34987.herokuapp.com/messages";

    class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton like;
        ToggleButton dislike;
        Button comment;
        TextView mTitle;
        TextView mMessage;
        TextView mVotes;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mMessage = (TextView) itemView.findViewById(R.id.listItemText);
            this.mVotes = (TextView) itemView.findViewById(R.id.listItemVotes);

            this.like = (ToggleButton) itemView.findViewById(R.id.likeButton);
            this.dislike = (ToggleButton) itemView.findViewById(R.id.dislikeButton);

            this.comment = (Button) itemView.findViewById(R.id.commentButton);
        }

    }


    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> mData;
    private LayoutInflater mLayoutInflater;

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
        View view = mLayoutInflater.inflate(R.layout.list_item, null, false);
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
        holder.mMessage.setText(d.mMessage);
        holder.mVotes.setText(d.mVotes + ""); //can only pass String


        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
//                Intent input = new Intent(MySingleton.getContext(), CommentActivity.class);
//                input.putExtra("label_contents", "CommentActivity on a message");
//                (new MainActivity()).startActivityForResult(input, 789);
            }
        });

        final View.OnClickListener likeButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent input = new Intent(MySingleton.getContext(), MainActivity.class);
//                input.putExtra("label_contents", "like a message");
//                (new MainActivity()).startActivityForResult(input, 789);

                String voteType;
//                if(d.liked) {
                voteType = "/downVote/";
//                } else {
//                    voteType = "/upVote/";
//                    if(d.disliked){
//                        sendRoute(d.mIndex, voteType);
//                    }
//                }
//                d.liked = !d.liked;
                sendRoute(d.mIndex, voteType);
            }
        };

        final View.OnClickListener dislikeButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voteType;
//                if(d.disliked) {
//                voteType = "/upVote/";

//                } else {
                    voteType = "/downVote/";
//                    if(d.liked){
//                        sendRoute(d.mIndex, voteType);
//                    }
//                }
//                d.disliked = !d.disliked;
                sendRoute(d.mIndex, voteType);
//                adapter.notifyDataSetChanged();
            }

        };

        holder.like.setOnClickListener(likeButton);
        holder.dislike.setOnClickListener(dislikeButton);

    }



    void sendRoute(int index, String voteType){
        Log.d("lyle", "HERE");
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url + voteType + index, new Response.Listener<String>() {
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