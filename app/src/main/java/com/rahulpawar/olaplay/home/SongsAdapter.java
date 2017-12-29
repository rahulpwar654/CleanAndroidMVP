package com.rahulpawar.olaplay.home;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rahulpawar.olaplay.Constants;
import com.rahulpawar.olaplay.R;
import com.rahulpawar.olaplay.models.CityListData;
import com.rahulpawar.olaplay.models.SongData;
import com.rahulpawar.olaplay.nowplaying.NowPlayingActivity;
import com.rahulpawar.olaplay.player.MusicPlayerService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by warlord on 12/17/2017.
 */

public class SongsAdapter  extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {


    //private final SongsAdapter.OnItemClickListener listener;
    private List<SongData> originalData;
    private List<SongData> filtereddata;
    private Context context;
    DownloadManager downloadManager;
    int nowplayingid=-1;

    private ItemFilter mFilter = new ItemFilter();

    public Filter getFilter() {
        return mFilter;
    }



    public SongsAdapter(Context context, List<SongData> originalData/*, SongsAdapter.OnItemClickListener listener*/) {
        this.originalData = originalData;
        this.filtereddata = originalData;
        //this.listener = listener;
        this.context = context;
        downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
    }


    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SongViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SongViewHolder holder, final int position) {
        final SongData songData= filtereddata.get(position);

            holder.songArtist.setText("Artist: " + songData.artists);
            holder.songTitle.setText(songData.song);
            Glide.with(context)
                    .load(songData.cover_image)
                    .into(holder.songImage);

            holder.songplay.setImageResource(songData.isPlaying?R.drawable.ic_pause_black_36dp
                    :R.drawable.ic_play_arrow_black_36dp);

            holder.songdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    downloadfile(context,songData);


                }
            });
            holder.songplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // downloadfile(context,songData);
                  if(songData.isPlaying){
                      Intent intent = new Intent(context, MusicPlayerService.class);
                      intent.putExtra(Constants.song, songData.url);
                      intent.putExtra(Constants.Action, Constants.MUSIC_PAUSE);
                      intent.putExtra(Constants.NowPlaying, songData.song);
                      context.startService(intent);
                      nowplayingid=-1;

                  }else {
                      if(nowplayingid!=-1)
                      {
                          originalData.get(nowplayingid).isPlaying=false;
                      }
                      //send Intent
                      Intent intent = new Intent(context, MusicPlayerService.class);
                      intent.putExtra(Constants.song, songData.url);
                      intent.putExtra(Constants.Action, Constants.MUSIC_PLAY);
                      intent.putExtra(Constants.NowPlaying, songData.song);
                      context.startService(intent);
                      nowplayingid=position;

                      Intent intent2 = new Intent(context, NowPlayingActivity.class);
                      intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK    );
                      context.startActivity(intent2);

                  }
                    //App.getInstance().addPlayer(songData.url);


                    songData.isPlaying=!songData.isPlaying;
                    originalData.get(position).isPlaying=songData.isPlaying;


                    notifyDataSetChanged();
                }
            });




    }

    private void downloadfile(Context context, SongData songData) {

        String folder_main = "OlaPlay";
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        Uri Download_Uri = Uri.parse(songData.url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle(""+songData.song);
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription(""+songData.artists);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,"Olaplay");
        File fdest=new File(f.getAbsoluteFile()+"/"+songData.song+".mp3");
        /*if (!fdest.exists()) {
            fdest.mkdirs();
        }*/
        request.setDestinationUri(Uri.fromFile(fdest));
        //Enqueue a new download and same the referenceId
        try {
            long downloadReference = downloadManager.enqueue(request);
            Toast.makeText(context, "Download Path :\n"+fdest.getPath(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){}



    }


    @Override
    public int getItemCount() {
        return filtereddata.size();
    }


    /*public interface OnItemClickListener {
        void onClick(CityListData Item);
    }*/

    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView fevorite,songdownload,songplay,songImage;
        TextView songArtist,songTitle;



        public SongViewHolder(View view) {
            super(view);
             fevorite = (ImageView) view.findViewById(R.id.fevorite);
             songArtist = (TextView) view.findViewById(R.id.songArtist);
             songdownload = (ImageView) view.findViewById(R.id.songdownload);
             songplay = (ImageView) view.findViewById(R.id.songplay);
             songTitle = (TextView) view.findViewById(R.id.songTitle);
             songImage = (ImageView) view.findViewById(R.id.songImage);

        }

    }


    /**
     * Filter Class
     */
    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            //for Empty string
            if(filterString.trim().isEmpty())
            {
                results.count=originalData.size();
                results.values=originalData;
                return results;
            }



            final List<SongData> list = originalData;
            int count = list.size();

            final List<SongData> nlist = new ArrayList<SongData>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).song;
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }



        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // TODO Auto-generated method stub
            filtereddata = (ArrayList<SongData>) results.values;
            notifyDataSetChanged();
        }

    }

}
