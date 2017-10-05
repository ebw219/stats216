package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.support.v7.util.DiffUtil;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import javax.naming.Context;

import android.content.Context;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    String url = "https://sleepy-dusk-34987.herokuapp.com/messages";

    class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton like;
        ToggleButton dislike;
        Button comment;
        TextView mTitle;
        TextView mText;
        TextView mVotes;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mText = (TextView) itemView.findViewById(R.id.listItemText);
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
    public long getItemId(int position) {
        return super.getItemId(position);
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final lyle.cse216.lehigh.edu.tutorialforlyle.Datum d = mData.get(position);
        holder.mTitle.setText(d.mTitle);
        holder.mText.setText(d.mMessage);
        holder.mVotes.setText(d.mVotes + ""); //can only pass String

        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };


        final View.OnClickListener likeButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lyle", "HERE");
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url + "/upVote/" + d.mIndex, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", response);
                        Log.d("lyle", "BUTTON PRESSED: " + d.mIndex);
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
        };


        final View.OnClickListener dislikeButton = new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Log.d("lyle", "DOWN");
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url + "/downVote/" + d.mIndex, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", response);
                        Log.d("lyle", "BUTTON PRESSED");
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

        };


        holder.like.setOnClickListener(likeButton);
        holder.dislike.setOnClickListener(dislikeButton);

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