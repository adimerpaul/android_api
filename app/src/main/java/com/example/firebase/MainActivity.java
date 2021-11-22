package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.firebase.models.Pokemon;
import com.example.firebase.models.PokemonRespuesta;
import com.example.firebase.pokeapi.PokeapiService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MYTAG";
    private List<String> cities= new ArrayList<>();
    ListView listView;
    ArrayAdapter <String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cities.add("Bolivia");
        cities.add("Chile");
        listView = (ListView) findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,cities);
        listView.setAdapter(adapter);

//        db.collection("cities")
////                .whereEqualTo("state", "CA")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.e(TAG, "Listen failed.", e);
//                            return;
//                        }
//                        cities.clear();
////                        List<String> cities = new ArrayList<>();
//                        for (QueryDocumentSnapshot doc : value) {
//                            if (doc.get("name") != null) {
//                                cities.add(doc.getString("name"));
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                        Log.e(TAG, "Current cites in CA: " + cities);
//                    }
//                });





        retrofit= new  Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        obtenerdatos();



//        CollectionReference cities = db.collection("cities");
//
//        Map<String, Object> data1 = new HashMap<>();
//        data1.put("name", "San Francisco");
//        data1.put("state", "CA");
//        data1.put("country", "USA");
//        data1.put("capital", false);
//        data1.put("population", 860000);
//        data1.put("regions", Arrays.asList("west_coast", "norcal"));
//        cities.document("SF").set(data1);
//
//        Map<String, Object> data2 = new HashMap<>();
//        data2.put("name", "Los Angeles");
//        data2.put("state", "CA");
//        data2.put("country", "USA");
//        data2.put("capital", false);
//        data2.put("population", 3900000);
//        data2.put("regions", Arrays.asList("west_coast", "socal"));
//        cities.document("LA").set(data2);
//
//        Map<String, Object> data3 = new HashMap<>();
//        data3.put("name", "Washington D.C.");
//        data3.put("state", null);
//        data3.put("country", "USA");
//        data3.put("capital", true);
//        data3.put("population", 680000);
//        data3.put("regions", Arrays.asList("east_coast"));
//        cities.document("DC").set(data3);
//
//        Map<String, Object> data4 = new HashMap<>();
//        data4.put("name", "Tokyo");
//        data4.put("state", null);
//        data4.put("country", "Japan");
//        data4.put("capital", true);
//        data4.put("population", 9000000);
//        data4.put("regions", Arrays.asList("kanto", "honshu"));
//        cities.document("TOK").set(data4);
//
//        Map<String, Object> data5 = new HashMap<>();
//        data5.put("name", "Beijing");
//        data5.put("state", null);
//        data5.put("country", "China");
//        data5.put("capital", true);
//        data5.put("population", 21500000);
//        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
//        cities.document("BJ").set(data5);


//// Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Isabel");
//        user.put("last", "Ayma");
//        user.put("born", 2000);
//
//// Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//
//
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.e(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Error adding document", e);
//                    }
//                });

    }

    private void obtenerdatos() {
        PokeapiService service=retrofit.create(PokeapiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall=service.obtenerListaPokemon();
        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                if (response.isSuccessful()){
                    PokemonRespuesta pokemonRespuesta=response.body();
                    ArrayList<Pokemon> listaPokemon= pokemonRespuesta.getResults();
//                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);
                    cities.clear();
                    for (int i=0;i<listaPokemon.size();i++){
                        Pokemon p=listaPokemon.get(i);
                        Log.e(TAG,"Pokemon: "+p.getName());
                        cities.add(p.getName());

                    }
                }else{
                    Log.e(TAG,"onResponse: "+response.errorBody());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                Log.e(TAG,"onFailure: "+t.getMessage());
            }
        });
    }
}