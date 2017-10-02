package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.support.v7.util.DiffUtil;
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

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    public class EmployeeDiffCallback extends DiffUtil.Callback {

        private final List<Datum> mOld;
        private final List<Datum> mNew;

        public EmployeeDiffCallback(List<Datum> old, List<Datum> newD) {
            this.mOld = old;
            this.mNew = newD;
        }

        @Override
        public int getOldListSize() {
            return mOld.size();
        }

        @Override
        public int getNewListSize() {
            return mNew.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button like;
        Button dislike;
        TextView mTitle;
        TextView mText;
        TextView mVotes;

        ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.titleNew);
            this.mText = (TextView) itemView.findViewById(R.id.listItemText);
            this.mVotes = (TextView) itemView.findViewById(R.id.listItemVotes);

            this.like = (Button) itemView.findViewById(R.id.likeButton);
            this.dislike = (Button) itemView.findViewById(R.id.dislikeButton);
        }
    }

    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Datum> mData;
    private LayoutInflater mLayoutInflater;

    public void updateListItems(List<Datum> posts) {
        final EmployeeDiffCallback diffCallback = new EmployeeDiffCallback(this.mData, posts);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mData.clear();
        this.mData.addAll(posts);
        diffResult.dispatchUpdatesTo(this);
    }

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
//        updateListItems(mData);
        final lyle.cse216.lehigh.edu.tutorialforlyle.Datum d = mData.get(position);
        holder.mTitle.setText(d.mTitle);
        holder.mText.setText(d.mMessage);
//        holder.mVotes.setText(d.mVotes);

        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };

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
    void setClickListener(ClickListener c) {
        mClickListener = c;
    }


}