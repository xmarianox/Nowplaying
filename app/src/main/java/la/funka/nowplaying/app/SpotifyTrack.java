package la.funka.nowplaying.app;

public class SpotifyTrack {
    private int cover;
    private String track_name;
    private String track_uri;

    public SpotifyTrack() {
        super();
    }

    public SpotifyTrack(int cover, String track_name, String track_uri) {
        super();
        this.cover = cover;
        this.track_name = track_name;
        this.track_uri = track_uri;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getTrack_uri() {
        return track_uri;
    }

    public void setTrack_uri(String track_uri) {
        this.track_uri = track_uri;
    }
}
