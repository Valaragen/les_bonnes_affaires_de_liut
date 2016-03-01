package com.acy.iut.fr.lesbonsplansdeliut;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
    //Declare fields
    private EditText nom,prenom, password,mail,tel,dep,login;


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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription, menu);
        return true;
    }
    // method when click on formPage
    public void inscriptionClick(View v) {
        if(v.getId() != R.id.btnInscription) {
            return;
        }
        new AttemptLogin().execute();
    }//convert an inputstream to a string
    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //async call to the php script
    class AttemptLogin extends AsyncTask<Credential, String, JSONObject> {
        private Utilisateur u = new Utilisateur(Integer.parseInt(dep.getText().toString()),nom.getText().toString(),prenom.getText().toString(),mail.getText().toString(),tel.getText().toString(),password.getText().toString(),login.getText().toString());


        //display loading and status
        protected void onPreExecute() {
        }

        //Get JSON data from the URL
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(Credential... args) {
            JSONObject json = null;
            try {
                Log.d("request!", "starting");
                URL url = null;
                HttpURLConnection connection = null;
                try {
                    //initialize connection
                    url = new URL(Main.LOGIN_URL);
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
                success = result.getInt(Main.FLAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
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

    //Click button

}
