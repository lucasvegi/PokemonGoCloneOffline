package teste.lucasvegi.pokemongooffline.Util;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

public class NearbySearch {

    public static PlacesSearchResponse run( LatLng latlng) {
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD_82FN8rMIJzMrZyx1l7xZbpW1SYN5pdU")
                .build();
        try {
            request = PlacesApi.nearbySearchQuery(context, latlng)
                    .rankby(RankBy.PROMINENCE)
                    .radius(300)
                    .await();
            return request;
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return request;
    }
}