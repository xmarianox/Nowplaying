package la.funka.nowplaying.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrackAdapter extends ArrayAdapter<SpotifyTrack> {

    Context context;
    int resource;
    ArrayList<SpotifyTrack> data = null;

    public TrackAdapter(Context context, int resource, ArrayList<SpotifyTrack> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View row = converView;
        SpotifyTrackHolder holder = null;

        if ( row == null ){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new SpotifyTrackHolder();
            holder.track_cover = (ImageView) row.findViewById(R.id.track_cover);
            holder.title_track = (TextView) row.findViewById(R.id.title_track);
            holder.label_uri = (TextView) row.findViewById(R.id.label_uri);

            row.setTag(holder);
        } else {
            holder = (SpotifyTrackHolder) row.getTag();
        }

        SpotifyTrack tracks = data.get(position);
        holder.title_track.setText(tracks.getTrack_name().toString());
        holder.label_uri.setText(tracks.getTrack_uri().toString());
        holder.track_cover.setImageResource(tracks.getCover());

        return row;
    }

    static class SpotifyTrackHolder {
        ImageView track_cover;
        TextView title_track;
        TextView label_uri;
    }
 }
