package com.acy.iut.fr.lesbonsplansdeliut;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acy.iut.fr.lesbonsplansdeliut.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Inscription extends Activity {

    //static fields for ease of access
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String LOGIN_URL = "http://rudyboinnard.esy.es/android/";

    //Declare fields
    private EditText nom,prenom, password,mail,tel,dep,login;
    private TextView testText;
    private ListView list_departement;
    String[]departement = new String[]{"Antoine","Benoit","Lucas","Youtub","Melissa","Doritos"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_inscription);

        //initialize all fields
        nom = (EditText)findViewById(R.id.nom_user);
        prenom = (EditText)findViewById(R.id.prenom_user);
        mail = (EditText)findViewById(R.id.mail_user);
        tel = (EditText)findViewById(R.id.tel_user);
        dep = (EditText)findViewById(R.id.dep_user);
        password = (EditText)findViewById(R.id.mdp_user);
        login = (EditText)findViewById(R.id.login_user);
        testText = (TextView)findViewById(R.id.testText);

        list_departement = (ListView)findViewById(R.id.list_dept);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription, menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Inscription.this,
                android.R.layout.simple_list_item_1, departement);
        list_departement.setAdapter(adapter);
        return true;
    }
    // method when click on formPage
    public void inscriptionClick(View v) {
        if(v.getId() != R.id.btnInscription) {
            return;
        }
        Log.d("DEBUG", "Click on inscription");
        if (dep.getText().toString().matches("") || nom.getText().toString().matches("") || prenom.getText().toString().matches("")|| mail.getText().toString().matches("") || tel.getText().toString().matches("")||  password.getText().toString().matches("") ||  login.getText().toString().matches("")) {
            Toast.makeText(this, "Remplir tous les champs pour continuer", Toast.LENGTH_SHORT).show();
        }else {
            new AddUser().execute();
        }

    }

    //convert an inputstream to a string
    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //async call to the php script
    class AddUser extends AsyncTask<Utilisateur, String, JSONObject> {

        private Utilisateur u = new Utilisateur(Integer.parseInt(dep.getText().toString()),nom.getText().toString(),prenom.getText().toString(),mail.getText().toString(),tel.getText().toString(),password.getText().toString(),login.getText().toString());

        //display loading and status
        protected void onPreExecute() {
            //testText.setText("Connecting...");
            Log.d("------DEBUG---",""+Integer.parseInt(dep.getText().toString()));
        }

        //Get JSON data from the URL
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(Utilisateur... args) {
            JSONObject json = null;
            try {
                Log.d("request!", "starting");
                URL url = null;
                HttpURLConnection connection = null;
                try {
                    //initialize connection
                    url = new URL(LOGIN_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    String urlParameters = "nom=" + u.getNom() + "&&prenom=" + u.getPrenom() + "&&departement=" + u.getId_departement()+ "&&mail=" + u.getMail()+ "&&telephone=" + u.getTel()+ "&&password=" + u.getMotdepasse()+ "&&login=" + u.getLogin()+ "&&method=" + "insertUser";
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                    //write post data to URL
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.write(postData);
                    //connect and get data
                    connection.connect();
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    json = new JSONObject(convertStreamToString(in));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //parse returned data
        protected void onPostExecute(JSONObject result) {
            int success = 0;

            try {
                //alert the user of the status of the connection
                success = result.getInt(FLAG_SUCCESS);
                Toast.makeText(Inscription.this, (String)result.getString(FLAG_MESSAGE),
                        Toast.LENGTH_LONG).show();
                //testText.setText(result.getString(FLAG_MESSAGE)+"");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                //e.printStackTrace();
            }
            //log the success status
            if (success == 1) {
                Log.d("OK", "OK");
            } else {
                Log.d("Error", "Error");
            }
        }
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

        return super.onOptionsItemSelected(item);
    }

}
