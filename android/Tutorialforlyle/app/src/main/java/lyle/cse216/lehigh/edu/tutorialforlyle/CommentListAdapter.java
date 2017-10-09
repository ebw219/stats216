package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {
        Button back;

        ViewHolder(View itemView) {
            super(itemView);
            this.back = (Button) itemView.findViewById(R.id.backButton);
        }

    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {

    }

    ArrayList<String> comments = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    CommentListAdapter(Context context, ArrayList<String> data) {
        comments = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null, false);
        return new CommentListAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }
}
