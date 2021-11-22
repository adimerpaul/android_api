package com.example.firebase.pokeapi;


import com.example.firebase.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeapiService {
    @GET("pokemon?limit=151&offset=0")
    Call<PokemonRespuesta> obtenerListaPokemon();
}
