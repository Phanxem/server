package com.natour.server.data.dao.implemented;



import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.PointDTO;
import com.natour.server.application.dtos.RouteDTO;
import com.natour.server.application.dtos.RouteLegDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.TODORouteException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
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
    
	
    private RouteDTO buildRouteDTO(JsonObject jsonObject) {

    	if(!jsonObject.has(KEY_CODE)) return null;
    	if(!jsonObject.get(KEY_CODE).getAsString().equals(OK_CODE)) return null;
    	if(!jsonObject.has(KEY_ROUTES) || !jsonObject.has(KEY_WAYPOINTS) ) return null;

    	JsonArray jsonArrayRoute = jsonObject.get(KEY_ROUTES).getAsJsonArray();
    	JsonObject jsonObjectRoute = jsonArrayRoute.get(0).getAsJsonObject();
    	JsonObject jsonObjectGeometry = jsonObjectRoute.get(KEY_GEOMETRY).getAsJsonObject();
    	JsonArray jsonArrayCoordinates = jsonObjectGeometry.get(KEY_COORDINATES).getAsJsonArray();
    	JsonArray jsonArrayLegs = jsonObjectRoute.get(KEY_LEGS).getAsJsonArray();
    	JsonArray jsonArrayWayPoints = jsonObject.get(KEY_WAYPOINTS).getAsJsonArray();
    	
    	List<PointDTO> wayPoints = new ArrayList<PointDTO>();
    	for(JsonElement jsonElementWayPoint: jsonArrayWayPoints) {
    		JsonObject jsonObjectWayPoint = jsonElementWayPoint.getAsJsonObject();
    		JsonArray jsonArrayLocation = jsonObjectWayPoint.get(KEY_LOCATION).getAsJsonArray();
    		
    		PointDTO pointDTO = new PointDTO();
    		pointDTO.setLon(jsonArrayLocation.get(0).getAsDouble());
    		pointDTO.setLat(jsonArrayLocation.get(1).getAsDouble());
    		
    		wayPoints.add(pointDTO);
    	}
    	
    	
    	List<RouteLegDTO> tracks = new ArrayList<RouteLegDTO>();
    	int j = 0;
    	for(int i = 0; i < jsonArrayLegs.size(); i++) {
    		RouteLegDTO routeLeg = new RouteLegDTO();
    		
    		routeLeg.setStartingPoint(wayPoints.get(i));
    		routeLeg.setDestinationPoint(wayPoints.get(i+1));
    		
    		JsonObject jsonObjectLeg = jsonArrayLegs.get(i).getAsJsonObject();
    		routeLeg.setDistance(jsonObjectLeg.get(KEY_DISTANCE).getAsFloat());
    		routeLeg.setDuration(jsonObjectLeg.get(KEY_DURATION).getAsFloat());
    		
    		
    		List<PointDTO> routeLegTrack = new ArrayList<PointDTO>();
    		for(; j < jsonArrayCoordinates.size(); j++) {
    			PointDTO pointDTO = new PointDTO();
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
    	
    	
    	RouteDTO routeDTO = new RouteDTO();
    	routeDTO.setWayPoints(wayPoints);
    	routeDTO.setTracks(tracks);
    	
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
				   + "&geometries=geojson";
		
		RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    	String jsonStringResult = response.getBody();  
    	
    	System.out.println("JSON-route:\n" + jsonStringResult);
    	
    	JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
        JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();
		
        
        
        RouteDTO result = buildRouteDTO(jsonObjectResult);
        
        if(result == null) throw new TODORouteException();
        
        
		return result;
	}
	

}
