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
import com.natour.server.application.dtos.response.GetAddressResponseDTO;
import com.natour.server.application.dtos.response.GetListAddressResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;
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
	
    
    private static final String KEY_LATITUDE = "lat";
    private static final String KEY_LONGITUDE = "lon";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ROAD = "road";
    private static final String KEY_POSTCODE = "postcode";
    private static final String KEY_CITY = "city";
    private static final String KEY_TOWN = "town";
    private static final String KEY_VILLAGE = "village";
    private static final String KEY_COUNTRY = "country";
    
    
    private GetAddressResponseDTO buildAddressDTO(JsonObject jsonObjectResult) {
    	
    	GetAddressResponseDTO addressDTO = new GetAddressResponseDTO();
        PointResponseDTO pointDTO = new PointResponseDTO();
        
    	if (!jsonObjectResult.has(KEY_LATITUDE) ||
            !jsonObjectResult.has(KEY_LONGITUDE) ||
            !jsonObjectResult.has(KEY_ADDRESS))
        {
    		addressDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
    		return addressDTO;
        }
    
        pointDTO.setLon(jsonObjectResult.get(KEY_LONGITUDE).getAsDouble());
        pointDTO.setLat(jsonObjectResult.get(KEY_LATITUDE).getAsDouble());
        addressDTO.setPoint(pointDTO);

        String addressLine = new String();
        JsonObject jsonObjectAddress = jsonObjectResult.get(KEY_ADDRESS).getAsJsonObject();
        
        if(jsonObjectAddress.has(KEY_ROAD)) {
        	addressLine = addressLine.concat(jsonObjectAddress.get(KEY_ROAD).getAsString() );
        }

        if(jsonObjectAddress.has(KEY_POSTCODE)) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get(KEY_POSTCODE).getAsString()); 
        }
            
        if(jsonObjectAddress.has(KEY_CITY)) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get(KEY_CITY).getAsString());
        }
        else if(jsonObjectAddress.has(KEY_TOWN)) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get(KEY_TOWN).getAsString());
        }
        else if(jsonObjectAddress.has(KEY_VILLAGE)) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get(KEY_VILLAGE).getAsString());
        }
            
        if(jsonObjectAddress.has(KEY_COUNTRY)) {
        	if(!addressLine.isEmpty()) addressLine = addressLine.concat(", ");
        	addressLine = addressLine.concat(jsonObjectAddress.get(KEY_COUNTRY).getAsString());
        }

        addressDTO.setAddressName(addressLine);
        addressDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
        
        return addressDTO;
    }
    
    
    
    
    
    
    public GetAddressResponseDTO findAddressByPoint(PointResponseDTO point) {
    	GetAddressResponseDTO result = null;
    	
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
        
        result = buildAddressDTO(jsonObjectResult);

    	return result;
    }
    
    public GetListAddressResponseDTO findAddressesByQuery(String query) {
    	GetListAddressResponseDTO listAddressResponseDTO = new GetListAddressResponseDTO();
    	
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
        	listAddressResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
        	return listAddressResponseDTO;
        }
        
        
        RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    	String jsonStringResult = response.getBody();    	
    	JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
        JsonArray jsonArrayResult = jsonElementResult.getAsJsonArray();
		
		List<GetAddressResponseDTO> addresses = new ArrayList<GetAddressResponseDTO>();
	
		for(int i = 0; i < jsonArrayResult.size(); i++){
			JsonObject jsonObjectResult = jsonArrayResult.get(i).getAsJsonObject();
            GetAddressResponseDTO address = buildAddressDTO(jsonObjectResult);
            if(address != null) addresses.add(address);
        }
        
		
		listAddressResponseDTO.setListAddress(addresses);
		listAddressResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
    	return listAddressResponseDTO;
    }
    




}
