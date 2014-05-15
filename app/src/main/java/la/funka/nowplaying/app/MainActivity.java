package la.funka.nowplaying.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends Activity {

    Button btn_buscar;
    EditText input_buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_buscar = (Button) findViewById(R.id.btn_buscar);
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_buscar = (EditText) findViewById(R.id.input_buscar);
                String query = input_buscar.getText().toString();

                try {
                    new BuscarTracks().execute("https://ws.spotify.com/search/1/track.json?q="+ URLEncoder.encode(query, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     *   BuscarTracks
     *
     *   https://spotifyapps.contenidos-digitales.com/mariano/app.php/search/track/
     *
     *   app.php/generate/data: { tipo: actividad, duracion: tiempo, track: track, name: userprofile.name, email: userprofile.email, id: userprofile.id }
     *
     *   https://spotifyapps.contenidos-digitales.com/absolut/app.php/push/
     *
     *   https://ws.spotify.com/search/1/track.json?q={nombre_track}
     *   new BuscarTracks().execute("URL");
     */
    public class BuscarTracks extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "Por favor espere...", "Descargando clima...", true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(urls[0]));
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null) {
                    BufferedReader buffer = new BufferedReader( new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = buffer.readLine()) != null)
                        result += line;
                    inputStream.close();
                } else {
                    // ERROR;
                }
            } catch (Exception e) {
                // ERROR;
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

/**
{
    info: {
        num_results: 142,
                limit: 100,
                offset: 0,
                query: "everlong",
                type: "track",
                page: 1
    },
    tracks: [
    {
        album: {
            released: "2009",
                    href: "spotify:album:1zCNrbPpz5OLSr6mSpPdKm",
                    name: "Greatest Hits",
                    availability: {
                territories: "AD AR AT AU BE BG BO BR CA CH CL CO CR CY CZ DE DK DO EC EE ES FI FR GB GR GT HK HN HR HU IE IS IT LI LT LU LV MC MT MX MY NI NL NO NZ PA PE PH PL PT PY RO SE SG SI SK SV TR TW US UY"
            }
        },
        name: "Everlong",
                popularity: "0.77",
            external-ids: [
        {
            type: "isrc",
                    id: "USRW29600011"
        }
        ],
        length: 249.986,
                href: "spotify:track:07q6QTQXyPRCf7GbLakRPr",
            artists: [
        {
            href: "spotify:artist:7jy3rLJdDQY21OgRLCZ9sD",
                    name: "Foo Fighters"
        }
        ],
        track-number: "3"
    },
*/
        @Override
        protected void onPostExecute(String resultado) {
            // En result está el texto que viene de Internet
            dialog.dismiss();

            try {
                JSONObject json = new JSONObject(resultado);
                JSONArray jsonArray = json.getJSONArray("tracks");

                //Toast.makeText(MainActivity.this, jsonArray.length(), Toast.LENGTH_SHORT).show();



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ocurrio un error...", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /* Traer imagenes desde internet.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView miImageView;

        public DownloadImageTask(ImageView bmImage) {
            this.miImageView = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            miImageView.setImageBitmap(result);
        }
    }
    // Cómo se usa
    //new DownloadImageTask((ImageView) findViewById(R.id.imageView1)).execute("url");
    */
}
