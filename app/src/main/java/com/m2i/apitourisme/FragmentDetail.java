package com.m2i.apitourisme;


import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.m2i.model.CoinTouriste;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetail extends Fragment implements AdapterView.OnItemClickListener {
    private List<CoinTouriste> ctList;
    private ListView lvSite;

    public FragmentDetail() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDataFromHttp();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        lvSite = view.findViewById(R.id.lvSite);

        lvSite.setOnItemClickListener(this);
        return view;
    }

    private void processResponse(String response){
        //Transformation de la réponse json en list de Coin touristique
        ctList = responseToList(response);

       // Conversion de la liste  en un tableau de String comportant
        //uniquement le nom du coin
        String[] data = new String[ctList.size()];
        for(int i =0; i < ctList.size(); i++){
            data[i] = ctList.get(i).getNom();
        }
        //Définition d'un ArrayAdapter pour alimenter la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                data
        );

        lvSite.setAdapter(adapter);

    }


    private void getDataFromHttp(){
        String url = "https://opendata.lillemetropole.fr/api/records/1.0/search/?dataset=points-de-vue-tourisme&rows=-1&apikey=229b4f85a160a91180b97f812eea04f1ef6514ce83bfb184f990d3bc";

        //Définition de la requête
        StringRequest request = new StringRequest(
                //Méthode de la requête http
                Request.Method.GET,
                url,
                //Gestionnaire de succès
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Résultat HTTP", response);
                        processResponse(response);
                    }
                },
                //Gestionnaire d'erreur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Anomalie equete HTTP", error.getMessage());
                    }
                }
        );

        //Ajout de la requête à la file d'exécution
        Volley  .newRequestQueue(this.getActivity())
                .add(request);
    }

    private List<CoinTouriste> responseToList(String response){
        List<CoinTouriste> list = new ArrayList<>();
        int t;
        t = 10;
        try {
            JSONObject ficJson = new JSONObject(response);
            JSONArray jsonCT = ficJson.getJSONArray("records");

            for(int i =0; i < jsonCT.length(); i++) {
                 JSONObject item = jsonCT.getJSONObject(i);

                // test l'existence de name dans l'occurence
                if (item.getJSONObject("fields").has("name")){
                    //Création d'un nouvel coint touristique
                    CoinTouriste ct = new CoinTouriste();
                    //hydratation du ct
                    ct.setId(item.getJSONObject("fields").getString("_id"));

                    ct.setNom(item.getJSONObject("fields").getString("name"));
                    JSONArray coord = item.getJSONObject("fields").getJSONArray("geo_point_2d");
                    ct.setLat((double)coord.get(0));
                    ct.setLat((double)coord.get(1));
                    list.add(ct);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CoinTouriste ct = ctList.get(i);
        Toast.makeText(getActivity() , ct.getNom(),Toast.LENGTH_LONG).show();
    }
}