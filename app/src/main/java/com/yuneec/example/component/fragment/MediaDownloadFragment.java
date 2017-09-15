package com.yuneec.example.component.fragment;

/**
 * Created by sushmas on 8/29/17.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.adapter.MediaListAdapter;
import com.yuneec.example.component.listeners.CameraListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.model.MediaInfoEntry;
import com.yuneec.sdk.Camera;

import java.io.File;
import java.util.ArrayList;

/**
 * This fragment allows users to view and download pictures and video taken from the drone
 */

public class MediaDownloadFragment extends Fragment implements
    SwipeRefreshLayout.OnRefreshListener {

    private View rootView;

    private Camera.MediaInfosListener mediaInfoslistener;
    private MediaListAdapter adapter;
    SwipeRefreshLayout swipeLayout;
    private boolean downloadingMedia = false;
    private Toast toast = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ArrayList<MediaInfoEntry> emptyList = new ArrayList<MediaInfoEntry>();

        adapter = new MediaListAdapter(getActivity(), emptyList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initViews(inflater, container);
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        //registerListener();
    }

    @Override
    public void onStop() {

        super.onStop();
        //unRegisterListener();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }


    private void initViews(LayoutInflater inflater,
                           ViewGroup container) {

        rootView = inflater.inflate(R.layout.media_download_layout, container, false);
        mediaInfoslistener = new Camera.MediaInfosListener() {
            @Override
            public void getMediaInfosCallback(final Camera.Result result,
                                              final ArrayList<Camera.MediaInfo> mediaInfos) {
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

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        ListView lv = (ListView) rootView.findViewById(R.id.media_info_list);
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
                File path = rootView.getContext().getExternalFilesDir(null);
                //System.out.println("Download to: " + path);
                // Create dir if not already existing
                if (path.mkdirs()) {
                    System.out.println("Created: " + path);
                } else {
                    System.out.println("Could not create: " + path);
                }
                return path + "/" + title;
            }

            private void downloadFile(final MediaInfoEntry entry) {

                // Ignore clicks while downloading something else, this is because we can
                // only have one listener at a time.
                if (downloadingMedia) {
                    return;
                }

                downloadingMedia = true;
                Camera.MediaListener mediaListener = new Camera.MediaListener() {
                    @Override
                    public void getMediaCallback(final Camera.Result result, final int progress) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (result.resultID != Camera.Result.ResultID.IN_PROGRESS) {

                                    updateToast("Download: " + result.resultStr);

                                    if (result.resultID == Camera.Result.ResultID.SUCCESS) {
                                        entry.downloaded = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    updateToast("Downloaded " + progress + " %");
                                }
                            }
                        });
                        downloadingMedia = false;
                    }

                };

                String localPath = localPath(entry.title);

                System.out.println("Fetching " + entry.path + " to " + localPath);

                // FIXME: Note that we overwrite the mediaListener here, so the old one will
                //        now be the same as the new one.
                Camera.getMediaAsync(localPath, entry.path, mediaListener);
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

                Context context = rootView.getContext();

                // Taken from https://stackoverflow.com/questions/38200282#answer-38858040
                newIntent.setDataAndType(FileProvider.getUriForFile(context,
                                                                    context.getApplicationContext().getPackageName() + ".yuneec.sdk.example.provider",
                                                                    file),
                                         mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

    }


    /*private void registerListener() {

        CameraListener.registerCameraListener(getActivity());
    }

    private void unRegisterListener() {

        CameraListener.unRegisterCameraListener();
    }*/

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
        Common.makeToast(getActivity(), text);
    }
}
