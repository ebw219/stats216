package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by emyweston on 11/6/17.
 */

public class MessageTitleListAdapter extends RecyclerView.Adapter<MessageTitleListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.message_title);
        }
    }


    private LayoutInflater mLayoutInflater;
//    private ArrayList<String> mTitles;
    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Titles> mTitles;

    MessageTitleListAdapter(Context context, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Titles> titles) {
       mTitles = titles;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MessageTitleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.user_profile, null);
        return new MessageTitleListAdapter.ViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Titles> t = mTitles;
        holder.title.setText((mTitles.get(position)).toString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
