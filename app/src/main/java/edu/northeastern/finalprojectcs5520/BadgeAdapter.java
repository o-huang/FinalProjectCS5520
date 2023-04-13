package edu.northeastern.finalprojectcs5520;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BadgeAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> mAchieved;

    public BadgeAdapter(Context context, ArrayList<Integer> achieved) {
        mContext = context;
        mAchieved = achieved;
    }

    @Override
    public int getCount() {
        return mAchieved.size();
    }

    @Override
    public Object getItem(int position) {
        return mAchieved.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_badge, parent, false);
        }

        ImageView badgeImage = view.findViewById(R.id.badge_image);
        TextView badgeNumber = view.findViewById(R.id.badge_number);

        int milestone = mAchieved.get(position);
        badgeNumber.setText(String.valueOf(milestone));

        return view;
    }
}
