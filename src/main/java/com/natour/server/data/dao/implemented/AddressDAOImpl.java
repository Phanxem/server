package com.natour.server.data.dao.implemented;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.natour.server.application.dtos.AddressDTO;
import com.natour.server.application.dtos.PointDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressSearchByPointFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressSearchByQueryFailureException;
import com.natour.server.data.dao.interfaces.AddressDAO;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressDAOImpl implements AddressDAO{

	private static final String NOMINATIM_SERVICE_URL = "https://nominatim.openstreetmap.org/";
    
	private static final String OPERATON_REVERSE = "reverse";
    private static final String OPERATION_SEARCH = "search";
    
    private static final int MAX_NUM_RESULTS = 20;
	
    
    private AddressDTO buildAddressDTO(JsonObject jsonObjectResult) {
    	
    	if (!jsonObjectResult.has("lat") ||
            !jsonObjectResult.has("lon") ||
            !jsonObjectResult.has("address"))
        {
            return null;    
        }
             
    	System.out.println("2");
    	
        AddressDTO addressDTO = new AddressDTO();
        PointDTO pointDTO = new PointDTO();
            

        pointDTO.setLon(jsonObjectResult.get("lon").getAsDouble());
        pointDTO.setLat(jsonObjectResult.get("lat").getAsDouble());
        addressDTO.setPoint(pointDTO);

        String addressLine = new String();
        JsonObject jsonObjectAddress = jsonObjectResult.get("address").getAsJsonObject();
        
        System.out.println("JSON:\n" + jsonObjectAddress);
        
        if(jsonObjectAddress.has("road")) {
        	addressLine = addressLine.concat(jsonObjectAddress.get("road").getAsString() );
        }

        if(jsonObjectAddress.has("postcode")) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get("postcode").getAsString()); 
        }
            
        if(jsonObjectAddress.has("city")) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get("city").getAsString());
        }
        else if(jsonObjectAddress.has("town")) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get("town").getAsString());
        }
        else if(jsonObjectAddress.has("village")) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get("village").getAsString());
        }
            
        if(jsonObjectAddress.has("country")) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get("country").getAsString());
        }

        addressDTO.setAddressLine(addressLine);
        return addressDTO;
    }
    
    public AddressDTO findAddressByPoint(PointDTO point) {
    	String url = NOMINATIM_SERVICE_URL + OPERATON_REVERSE
                + "?format=json"
                //+ "&accept-language=" + Locale.getDefault().getLanguage()
                + "&accept-language=it"
                + "&lon=" + point.getLon()
				+ "&lat=" + point.getLat();
    	
    	

    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    	String jsonStringResult = response.getBody();    	
    	JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
        JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();
        
        AddressDTO result = buildAddressDTO(jsonObjectResult);

    	return result;
    }
    
	
	@Override
	public List<AddressDTO> findAddressesByQuery(String query) {
		String url = null;
        try {
            url = NOMINATIM_SERVICE_URL + OPERATION_SEARCH
                    + "?format=json"
                    //+ "&accept-language=" + Locale.getDefault().getLanguage()
                    + "&accept-language=it"
                    + "&addressdetails=1"
                    + "&limit=" + MAX_NUM_RESULTS
                    + "&q=" + URLEncoder.encode(query,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
        	throw new AddressSearchByQueryFailureException(e);
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        CompletableFuture<List<AddressDTO>> completableFuture = new CompletableFuture<List<AddressDTO>>();

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
                JsonArray jsonArrayResult = jsonElementResult.getAsJsonArray();
				
				List<AddressDTO> addresses = new ArrayList<AddressDTO>();
			
				for(int i = 0; i < jsonArrayResult.size(); i++){
					JsonObject jsonObjectResult = jsonArrayResult.get(i).getAsJsonObject();
	                AddressDTO address = buildAddressDTO(jsonObjectResult);
	                if(address != null) addresses.add(address);
	            }
				 
				completableFuture.complete(addresses);
			}
        	
        });
        

        List<AddressDTO> results = null;

        try {
        	results = completableFuture.get();
		} 
        catch (InterruptedException | ExecutionException e) {
        	throw new AddressSearchByQueryFailureException(e);
		}
        
        if(results == null) throw new AddressSearchByQueryFailureException();
        
		return results;
	}





}
