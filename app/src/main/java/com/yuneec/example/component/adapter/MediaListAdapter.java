package com.yuneec.example.component.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuneec.example.component.fragment.CameraFragment;
import com.yuneec.example.model.MediaInfoEntry;
import com.yuneec.sdk.Camera;

import java.util.ArrayList;

/**
 * Created by sushmas on 8/29/17.
 */

public class MediaListAdapter
        extends BaseAdapter {

    private ArrayList<MediaInfoEntry> entries;

    private LayoutInflater inflater;

    public MediaListAdapter(Context context,
                            ArrayList<MediaInfoEntry> list) {

        entries = list;
        inflater = LayoutInflater.from(context);
    }

    public MediaInfoEntry entryFromMediaInfo(Camera.MediaInfo mediaInfo) {

        MediaInfoEntry entry = new MediaInfoEntry();
        entry.path = mediaInfo.path;
        entry.description = String.format("%.1f MiB", mediaInfo.size_mib);
        // We want to split "100MEDIA/YUN00001.jpg" to "YUN00001.jpg"
        String[] parts = entry.path.split("/");
        entry.title = parts[1];

        return entry;
    }

    public void setEntries(ArrayList<Camera.MediaInfo> list) {

        entries.clear();
        for (Camera.MediaInfo item : list) {
            entries.add(entryFromMediaInfo(item));
        }
    }

    @Override
    public int getCount() {

        return entries.size();
    }

    @Override
    public MediaInfoEntry getItem(int index) {

        return entries.get(index);
    }

    @Override
    public long getItemId(int index) {

        return index;
    }

    public View getView(int index,
                        View convertView,
                        ViewGroup parent) {

        MediaListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);
            holder = new MediaListAdapter.ViewHolder();
            holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(holder);
        } else {
            holder = (MediaListAdapter.ViewHolder) convertView.getTag();
        }

        MediaInfoEntry entry = entries.get(index);
        holder.text1.setText(entry.title);
        holder.text2.setText(entry.description);

        if (entry.downloaded) {
            convertView.setBackgroundColor(Color.argb(20, 0, 0, 255));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    class ViewHolder {

        TextView text1, text2;
    }
}
