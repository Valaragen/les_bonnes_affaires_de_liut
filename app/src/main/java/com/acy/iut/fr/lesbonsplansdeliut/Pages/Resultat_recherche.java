package com.acy.iut.fr.lesbonsplansdeliut.Pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.acy.iut.fr.lesbonsplansdeliut.Objets.Objet;
import com.acy.iut.fr.lesbonsplansdeliut.R;

import java.util.ArrayList;
import java.util.List;

public class Resultat_recherche extends Activity {

    ListView result_listView;
    List<Objet> result_List = new ArrayList<Objet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_recherche);
       // result_listView = (ListView) findViewById(R.id.result_listView);
        //TEST
        result_List.add(new Objet("Portable", "tres bon etat", 100));
        result_List.add(new Objet("Chien", "tres bon etat", 200));
        result_List.add(new Objet("Chat", "un peu usé", 120));

        for(Objet o : result_List){

        }
    }

    public void AddObjectClick(View v){
        Intent resultat_to_addObj = new Intent(Resultat_recherche.this, AddObject.class);
        startActivity(resultat_to_addObj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultat_recherche, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
