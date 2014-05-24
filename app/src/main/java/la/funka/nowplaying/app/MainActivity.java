package la.funka.nowplaying.app;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends ListActivity {

    Button btn_buscar;
    EditText input_buscar;
    ArrayList<String> tracks;
    ArrayAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracks = new ArrayList<String>();

        btn_buscar = (Button) findViewById(R.id.btn_buscar);
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_buscar = (EditText) findViewById(R.id.input_buscar);
                String query = input_buscar.getText().toString();

                try {
                    new BuscarTracks().execute("http://spotifyapps.contenidos-digitales.com/mariano/mobile_app.php/search/track/"+ URLEncoder.encode(query, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tracks);
        setListAdapter(adaptador);

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
 * https://spotifyapps.contenidos-digitales.com/mariano/mobile_app.php/search/track/ {query}
 *
 [
     {
        id: "spotify:track:07q6QTQXyPRCf7GbLakRPr",
        label: "Foo Fighters - Everlong",
        value: "Foo Fighters - Everlong",
        availability: "AD AR AT AU BE BG BO BR CA CH CL CO CR CY CZ DE DK DO EC EE ES FI FR GB GR GT HK HN HR HU IE IS IT LI LT LU LV MC MT MX MY NI NL NO NZ PA PE PH PL PT PY RO SE SG SI SK SV TR TW US UY"
     },
     {
        id: "spotify:track:3QmesrvdbPjwf7i40nht1D",
        label: "Foo Fighters - Everlong - Acoustic Version",
        value: "Foo Fighters - Everlong - Acoustic Version",
        availability: "AD AR AT AU BE BG BO BR CA CH CL CO CR CY CZ DE DK DO EC EE ES FI FR GB GR GT HK HN HR HU IE IS IT LI LT LU LV MC MT MX MY NI NL NO NZ PA PE PH PL PT PY RO SE SG SI SK SV TR TW US UY"
    }
 ]
 *
*/
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

                    tracks.add(label);
                }

                adaptador.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ocurrio un error...", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
