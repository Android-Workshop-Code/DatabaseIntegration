package aditya.com.databaseintegration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Code to retrieve data from database in android.
 */

public class RetrieveData extends ActionBarActivity {

    public static String url = "http://splitmoney.site50.net/workshop_retrieve.php";
    ListView listView;
    InputStream is = null;
    String exceptionMessage = "There seems to be some problem connecting to database. \" +\n" +
            "            \"Please check your Internet Connection and try again.";
    String result = "", line = "";
    String[] name;
    String[] email;
    String[] phone;
    String[] combinedArray;
    String names;
    String emails;
    String phones;
    String combinedText = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        // StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(threadPolicy);

        listView = (ListView) findViewById(R.id.listView1);

        new RetrieveTask().execute();
    }

    private class RetrieveTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), exceptionMessage+", Ex1", Toast.LENGTH_SHORT).show();
            }try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while((line=reader.readLine())!=null){
                    sb.append(line+"\n");
                }
                result = sb.toString();
                System.out.println("-----JSON Data-----");
                System.out.println(result);
                is.close();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), exceptionMessage+", Ex2", Toast.LENGTH_SHORT).show();
            }try{
                JSONArray jsonArray = new JSONArray(result);
                int totalCount = jsonArray.length();
                for(int i=0; i<totalCount; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    names += jsonObject.getString("name")+":";
                    emails += jsonObject.getString("email")+":";
                    phones += jsonObject.getString("phone")+":";
                    combinedText += (i+1)+". Name '"+jsonObject.getString("name")+"', "+
                            "Email '"+jsonObject.getString("email")+"', "+
                            "Phone '"+jsonObject.getString("phone")+"':";
                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), exceptionMessage+", Ex3", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            name = names.split(":");
            email = emails.split(":");
            phone = phones.split(":");
            combinedArray = combinedText.split(":");

            listView.setAdapter(new ArrayAdapter<String>(RetrieveData.this,
                    android.R.layout.simple_list_item_1, combinedArray));

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RetrieveData.this);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.setIcon(R.drawable.ic_play_for_work_black_24dp);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_retrieve_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_refresh){
            Intent it = new Intent(RetrieveData.this, RetrieveData.class);
            startActivity(it);

        }

        return super.onOptionsItemSelected(item);
    }
}
