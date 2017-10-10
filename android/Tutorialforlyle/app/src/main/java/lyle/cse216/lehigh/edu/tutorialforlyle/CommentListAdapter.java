package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment;

        ViewHolder(View itemView) {
            super(itemView);
            this.comment = (TextView) itemView.findViewById(R.id.commentItemComment);
        }

    }

    ArrayList<String> comments = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    CommentListAdapter(Context context, ArrayList<String> data) {
        comments = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.comment_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {
        final String d = comments.get(position);
        holder.comment.setText(d);
    }



    @Override
    public int getItemCount() {
        return comments.size();
    }
}



//    Context context = MySingleton.getContext();
//        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(getRequest);