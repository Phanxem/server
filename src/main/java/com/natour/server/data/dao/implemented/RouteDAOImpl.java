package com.natour.server.data.dao.implemented;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.natour.server.application.dtos.PointDTO;
import com.natour.server.application.dtos.RouteDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.RouteSearchByCoordinatesFailureException;
import com.natour.server.data.dao.interfaces.RouteDAO;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteDAOImpl implements RouteDAO{

	private static final String ROUTING_SERVICE_URL = "https://routing.openstreetmap.de/";
    private static final String MEAN_BY_FOOT = "routed-foot/route/v1/driving/";
	
    private RouteDTO buildRouteDTO(JsonObject jsonObjectResult) {
		
    	if (!jsonObjectResult.has("distance") ||
            !jsonObjectResult.has("duration") ||
            !jsonObjectResult.has("geometry"))
        {
            return null;    
        }
    	
    	
    	RouteDTO routeDTO = new RouteDTO();
    	
    	routeDTO.setDistance(jsonObjectResult.get("distance").getAsFloat());
    	routeDTO.setDuration(jsonObjectResult.get("duration").getAsFloat());
    	
    	
    	JsonObject jsonObjectGeometry = jsonObjectResult.get("geometry").getAsJsonObject();
    	JsonArray jsonArrayPoints = jsonObjectGeometry.get("coordinates").getAsJsonArray();
    	
    	
    	List<PointDTO> geometry = new ArrayList<PointDTO>();
    	PointDTO point;
    	JsonArray jsonArrayCoordinates;
    	for(int i = 0; i < jsonArrayPoints.size(); i++){
    		jsonArrayCoordinates = jsonArrayPoints.get(i).getAsJsonArray();
    		
    		point = new PointDTO(jsonArrayCoordinates.get(0).getAsDouble(),
    							 jsonArrayCoordinates.get(1).getAsDouble());
    		
    		geometry.add(point);
        }
    	
    	routeDTO.setGeometry(geometry);
    	
    	return routeDTO;
    	
    }
    
	@Override
	public RouteDTO findRouteByPoints(List<PointDTO> points) {
		return null;
	}

	@Override
	public RouteDTO findRouteByCoordinates(String coordinates) {
		
		String url = ROUTING_SERVICE_URL + MEAN_BY_FOOT
				   + coordinates
				   + "?overview=full"
				   + "&steps=true";
		
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        CompletableFuture<RouteDTO> completableFuture = new CompletableFuture<RouteDTO>();

        call.enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				completableFuture.complete(null);
				return;
				
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(!response.isSuccessful()) {
					completableFuture.complete(null);
					return;
				}
				
				String jsonStringResult = response.body().string();
                JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();

                RouteDTO result = buildRouteDTO(jsonObjectResult);

                completableFuture.complete(result);			
			} 	
        });


        RouteDTO result = null;
        try {
        	result = completableFuture.get();
		}
        catch (InterruptedException | ExecutionException e) {
        	throw new RouteSearchByCoordinatesFailureException(e);
		}
        
        if(result == null) throw new RouteSearchByCoordinatesFailureException();
        
		return result;
	}


}
