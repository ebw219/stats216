package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

//import javax.naming.Context;

import android.content.Context;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        Button like;
        Button dislike;
        TextView mTitle;
        TextView mText;
        TextView mVote;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mText = (TextView) itemView.findViewById(R.id.listItemText);
            this.mVote = (TextView) itemView.findViewById(R.id.listItemVotes);

            this.like = (Button) itemView.findViewById(R.id.likeButton);
            this.dislike = (Button) itemView.findViewById(R.id.dislikeButton);
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final lyle.cse216.lehigh.edu.tutorialforlyle.Datum d = mData.get(position);
        holder.mTitle.setText(d.mTitle);
        holder.mText.setText(d.mMessage);
//        holder.mVote.setText(d.mVotes);

        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };
//        holder.mTitle.setOnClickListener(listener);
//        holder.mText.setOnClickListener(listener);
//        holder.mVote.setOnClickListener(listener);

        final View.OnClickListener buttonListen = new View.OnClickListener() {

            @Override
            public void onClick(View view){

            }

        };

        holder.like.setOnClickListener(listener);
        holder.dislike.setOnClickListener(listener);
    }

    interface ClickListener{
        void onClick(Datum d);
    }
    private ClickListener mClickListener;
    ClickListener getClickListener() {return mClickListener;}
    void setClickListener(ClickListener c) { mClickListener = c;}
}