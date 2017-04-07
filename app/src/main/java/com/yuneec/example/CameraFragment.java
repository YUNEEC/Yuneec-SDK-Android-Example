/**
 * CameraFragment.java
 * Yuneec-SDK-Android-Example
 *
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.yuneec.sdk.Camera;

import java.io.File;
import java.util.ArrayList;

public class CameraFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View rootView;
    private Camera.MediaInfosListener mediaInfoslistener;
    private ListviewMediaInfosAdapter adapter;
    SwipeRefreshLayout swipeLayout;
    private boolean downloadingMedia = false;
    private Toast toast = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<MediaInfoEntry> emptyList = new ArrayList<MediaInfoEntry>();

        adapter = new ListviewMediaInfosAdapter(getActivity(), emptyList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaInfoslistener = new Camera.MediaInfosListener() {
            @Override
            public void getMediaInfosCallback(final Camera.Result result, final ArrayList<Camera.MediaInfo> mediaInfos) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.setEntries(mediaInfos);
                        adapter.notifyDataSetChanged();
                        updateToast(result.resultStr + ", found " + mediaInfos.size() + " media items");
                        swipeLayout.setRefreshing(false);
                    }
                });
            }
        };

        rootView = inflater.inflate(R.layout.camera_example, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        ListView lv = (ListView)rootView.findViewById(R.id.media_info_list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                final MediaInfoEntry entry = adapter.getItem(i);

                if (entry.downloaded) {
                    openFile(entry);
                } else {
                    downloadFile(entry);
                }
            }

            private String localPath(String title) {
                // TODO: should check if there is a SD card inserted.
                return "/storage/sdcard1/" + title;
            }

            private void downloadFile(final MediaInfoEntry entry) {

                // Ignore clicks while downloading something else, this is because we can
                // only have one listener at a time.
                if(downloadingMedia) {
                    return;
                }

                downloadingMedia = true;
                Camera.MediaListener mediaListener = new Camera.MediaListener() {
                    @Override
                    public void getMediaCallback(final Camera.Result result, final long bytes, final long bytesTotal) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (result.resultID != Camera.Result.ResultID.IN_PROGRESS) {

                                    updateToast("Download: " + result.resultStr);

                                    if (result.resultID == Camera.Result.ResultID.SUCCESS) {
                                        entry.downloaded = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    double percent = (double)bytes * 100.0 / (double)bytesTotal;
                                    updateToast("Downloaded " + String.format("%d", (int)percent) + " %");
                                }
                            }
                        });
                        downloadingMedia = false;
                    }

                };

                String localPath = localPath(entry.title);

                System.out.println("Fetching "+entry.path +" to "+localPath);

                // FIXME: Note that we overwrite the mediaListener here, so the old one will
                //        now be the same as the new one.
                Camera.getMediaAsync(localPath,entry.path,mediaListener);
            }

            private void openFile(MediaInfoEntry entry) {

                // Some file open magic taken from:
                //http://stackoverflow.com/questions/6265298#answer-6381479

                String localPath = localPath(entry.title);
                File file = new File(localPath);

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);

                String fileExtension = fileExt(localPath);
                String mimeType = myMime.getMimeTypeFromExtension(fileExtension);

                newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    rootView.getContext().startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    updateToast("No handler for this type of file.");
                }
            }

            private String fileExt(String url) {
                if (url.indexOf("?") > -1) {
                    url = url.substring(0, url.indexOf("?"));
                }
                if (url.lastIndexOf(".") == -1) {
                    return null;
                } else {
                    String ext = url.substring(url.lastIndexOf(".") + 1);
                    if (ext.indexOf("%") > -1) {
                        ext = ext.substring(0, ext.indexOf("%"));
                    }
                    if (ext.indexOf("/") > -1) {
                        ext = ext.substring(0, ext.indexOf("/"));
                    }
                    return ext.toLowerCase();
                }
            }
        });


        // Trigger a manual refresh when the view is created.
        swipeLayout.setRefreshing(true);
        refreshIndex();

        return rootView;
    }

    public static class MediaInfoEntry  {
        public String path;
        public String title;
        public String description;
        public boolean downloaded = false;
    }

    public class ListviewMediaInfosAdapter extends BaseAdapter {
        private ArrayList<MediaInfoEntry> entries;
        private LayoutInflater inflater;

        public ListviewMediaInfosAdapter(Context context, ArrayList<MediaInfoEntry> list) {
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

        public View getView(int index, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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

    @Override
    public void onRefresh() {
        refreshIndex();
    }

    public void refreshIndex() {
        Camera.getMediaInfosAsync(mediaInfoslistener);
    }

    private void updateToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(rootView.getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
