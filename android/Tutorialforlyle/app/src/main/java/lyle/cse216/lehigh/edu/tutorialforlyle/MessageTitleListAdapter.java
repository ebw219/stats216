package lyle.cse216.lehigh.edu.tutorialforlyle;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by emyweston on 11/6/17.
 *
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
    private ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Titles> mTitles;

    MessageTitleListAdapter(Context context, ArrayList<lyle.cse216.lehigh.edu.tutorialforlyle.Titles> titles) {
        mTitles = titles;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MessageTitleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.user_profile, null);
        RequestQueue queue = MySingleton.getInstance(MySingleton.getContext()).getRequestQueue();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final lyle.cse216.lehigh.edu.tutorialforlyle.Titles t = mTitles.get(position);
        holder.title.setText(t.mTitle);
//        Intent msglist = new Intent(MySingleton.getContext(), UserProfPageActivity.class);
//        msglist.putExtra("title", holder.title + "");
        Log.d("lyle", "title put in intent: " + holder.title);
//        MySingleton.getContext().startActivity(msglist);

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}