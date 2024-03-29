package com.natour.server.data.dao.implemented;



import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.dtos.response.GetRouteLegResponseDTO;
import com.natour.server.application.dtos.response.GetRouteResponseDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.interfaces.RouteDAO;


public class RouteDAOImpl implements RouteDAO{

	private static final String ROUTING_SERVICE_URL = "https://routing.openstreetmap.de/";
    private static final String MEAN_BY_FOOT = "routed-foot/route/v1/driving/";
    
    private static final String KEY_CODE = "code";
    private static final String OK_CODE = "Ok";
    private static final String KEY_ROUTES = "routes";
    private static final String KEY_WAYPOINTS = "waypoints";
    private static final String KEY_GEOMETRY = "geometry";
    private static final String KEY_COORDINATES = "coordinates";
    private static final String KEY_LEGS = "legs";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_DURATION = "duration";
    
	
    private GetRouteResponseDTO buildRouteDTO(JsonObject jsonObject) {

    	GetRouteResponseDTO routeDTO = new GetRouteResponseDTO();
    	
    	
    	if(!jsonObject.has(KEY_CODE) ||
    	   !jsonObject.get(KEY_CODE).getAsString().equals(OK_CODE) ||
    	   !jsonObject.has(KEY_ROUTES) ||
    	   !jsonObject.has(KEY_WAYPOINTS) )
    	{
    		routeDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
    		return routeDTO;
    	}

    	JsonArray jsonArrayRoute = jsonObject.get(KEY_ROUTES).getAsJsonArray();
    	JsonObject jsonObjectRoute = jsonArrayRoute.get(0).getAsJsonObject();
    	JsonObject jsonObjectGeometry = jsonObjectRoute.get(KEY_GEOMETRY).getAsJsonObject();
    	JsonArray jsonArrayCoordinates = jsonObjectGeometry.get(KEY_COORDINATES).getAsJsonArray();
    	JsonArray jsonArrayLegs = jsonObjectRoute.get(KEY_LEGS).getAsJsonArray();
    	JsonArray jsonArrayWayPoints = jsonObject.get(KEY_WAYPOINTS).getAsJsonArray();
    	
    	List<PointResponseDTO> wayPoints = new ArrayList<PointResponseDTO>();
    	for(JsonElement jsonElementWayPoint: jsonArrayWayPoints) {
    		JsonObject jsonObjectWayPoint = jsonElementWayPoint.getAsJsonObject();
    		JsonArray jsonArrayLocation = jsonObjectWayPoint.get(KEY_LOCATION).getAsJsonArray();
    		
    		PointResponseDTO pointDTO = new PointResponseDTO();
    		pointDTO.setLon(jsonArrayLocation.get(0).getAsDouble());
    		pointDTO.setLat(jsonArrayLocation.get(1).getAsDouble());
    		
    		wayPoints.add(pointDTO);
    	}
    	
    	
    	List<GetRouteLegResponseDTO> tracks = new ArrayList<GetRouteLegResponseDTO>();
    	int j = 0;
    	for(int i = 0; i < jsonArrayLegs.size(); i++) {
    		GetRouteLegResponseDTO routeLeg = new GetRouteLegResponseDTO();
    		
    		routeLeg.setStartingPoint(wayPoints.get(i));
    		routeLeg.setDestinationPoint(wayPoints.get(i+1));
    		
    		JsonObject jsonObjectLeg = jsonArrayLegs.get(i).getAsJsonObject();
    		routeLeg.setDistance(jsonObjectLeg.get(KEY_DISTANCE).getAsFloat());
    		routeLeg.setDuration(jsonObjectLeg.get(KEY_DURATION).getAsFloat());
    		
    		
    		List<PointResponseDTO> routeLegTrack = new ArrayList<PointResponseDTO>();
    		for(; j < jsonArrayCoordinates.size(); j++) {
    			PointResponseDTO pointDTO = new PointResponseDTO();
    			JsonArray jsonArrayPoint = jsonArrayCoordinates.get(j).getAsJsonArray();
    			
    			pointDTO.setLon(jsonArrayPoint.get(0).getAsDouble());
    			pointDTO.setLat(jsonArrayPoint.get(1).getAsDouble());
    			
    			routeLegTrack.add(pointDTO);
    			
    			if(pointDTO.getLon() == wayPoints.get(i+1).getLon() &&
    			   pointDTO.getLat() == wayPoints.get(i+1).getLat() )
    			{
    				break;
    			}
    		}
    		routeLeg.setTrack(routeLegTrack);
    		tracks.add(routeLeg);
    	}
    	
    	
    	routeDTO.setWayPoints(wayPoints);
    	routeDTO.setTracks(tracks);
    	routeDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
    	
    	return routeDTO;
    }
    

	@Override
	public GetRouteResponseDTO findRouteByCoordinates(String coordinates) {
		String url = ROUTING_SERVICE_URL + MEAN_BY_FOOT
				   + coordinates
				   + "?overview=full"
				   + "&geometries=geojson";
		
		System.out.println(url);
		
		RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    	String jsonStringResult = response.getBody();  
    	
    	System.out.println("JSON-route:\n" + jsonStringResult);
    	
    	JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
        JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();
		
        
        
        GetRouteResponseDTO result = buildRouteDTO(jsonObjectResult);

		return result;
	}
	

}
