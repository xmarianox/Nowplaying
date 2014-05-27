package la.funka.nowplaying.app;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;


public class ResultadoActivity extends ListActivity {

    TrackAdapter adaptador;
    ArrayList<SpotifyTrack> tracks_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        tracks_list = new ArrayList<SpotifyTrack>();

        String query = getIntent().getStringExtra("track");

        try {
            new BuscarTracks().execute("http://spotifyapps.contenidos-digitales.com/mariano/mobile_app.php/search/track/"+ URLEncoder.encode(query, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        adaptador = new TrackAdapter(this, R.layout.list_item, tracks_list);
        setListAdapter(adaptador);
    }

    /**
     *   BuscarTracks:
     *   https://spotifyapps.contenidos-digitales.com/mariano/mobile_app.php/search/track/ {query}
     *   -------
     *   Generar:
     *   app.php/generate/data: { tipo: actividad, duracion: tiempo, track: track, name: userprofile.name, email: userprofile.email, id: userprofile.id }
     *   -------
     *   Agregar:
     *   https://spotifyapps.contenidos-digitales.com/absolut/app.php/push/
     */
    public class BuscarTracks extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ResultadoActivity.this, "Por favor espere...", "Buscando el track...", true);
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

        @Override
        protected void onPostExecute(String resultado) {
            // En result está el texto que viene de Internet
            dialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(resultado);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject listadoJson = jsonArray.getJSONObject(i);
                    String uri = listadoJson.getString("id");
                    String label = listadoJson.getString("label");
                    tracks_list.add(new SpotifyTrack(R.drawable.vinil, label, uri));
                }
                adaptador.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ResultadoActivity.this, "Ocurrio un error al buscar el track...", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
