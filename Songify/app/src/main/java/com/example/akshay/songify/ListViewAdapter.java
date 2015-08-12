package com.example.akshay.songify;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Songs> songlist = null;
    private ArrayList<Songs> arraylist;

    public ListViewAdapter(Context context, List<Songs> songlist) {
        mContext = context;
        this.songlist = songlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Songs>();
        this.arraylist.addAll(songlist);
    }

    public class ViewHolder {
        TextView id;
        TextView title;
        TextView artist;
        TextView album;
    }

    @Override
    public int getCount() {
        return songlist.size();
    }

    @Override
    public Songs getItem(int position) {
        return songlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            Log.i("getView","called");
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.songs_list, null);
            // Locate the TextViews in listview_item.xml
            holder.id = (TextView)view.findViewById(R.id.textViewitemid);
            holder.title = (TextView) view.findViewById(R.id.textViewitemtitle);
            holder.artist = (TextView) view.findViewById(R.id.textViewitemartist);
            holder.album = (TextView) view.findViewById(R.id.textViewitemalbum);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.id.setText(""+songlist.get(position).getId());
        holder.title.setText(songlist.get(position).getTitle());
        holder.artist.setText(songlist.get(position).getArtist());
        holder.album.setText(songlist.get(position).getAlbum());

        /*Log.i("Holder value", "" + songlist.get(0).get());
        Log.i("Holder value", "" + songlist.get(0).getId());
        Log.i("Holder value", "" + songlist.get(0).getartist());
        Log.i("Holder value", "" + songlist.get(0).getalbum());*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        songlist.clear();
        if (charText.length() == 0) {
            songlist.addAll(arraylist);
        }
        else
        {
            for (Songs s : arraylist)
            {
                if (s.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || s.getArtist().toLowerCase(Locale.getDefault()).contains(charText) || s.getAlbum().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    songlist.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

}