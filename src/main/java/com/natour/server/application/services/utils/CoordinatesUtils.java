package com.natour.server.application.services.utils;


import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;

public class CoordinatesUtils {

	public final static double MIN_LON = -180;
	public final static double MAX_LON = 180;
	public final static double MIN_LAT = -90;
	public final static double MAX_LAT = 90;
	
	
	public static boolean arePointCoordinatesValid(String coordinates) {
		
		String[] stringCoordinates = coordinates.split(",");
		
		if(stringCoordinates.length != 2) {
			return false;
		}
		
		Double lon = null;
		Double lat = null;
		try {
			lon = Double.parseDouble(stringCoordinates[0]); 
			lat = Double.parseDouble(stringCoordinates[1]);	
		}
		catch(NumberFormatException e) {
			return false;
		}
		
		if(lon < MIN_LON ||
		   lon > MAX_LON ||
		   lat < MIN_LAT ||
		   lat > MAX_LAT )
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean areRouteCoordinatesValid(String coordinates) {
		
		String[] stringPoints = coordinates.split(";");
		
		for(String stringPoint: stringPoints) {
			if(!arePointCoordinatesValid(stringPoint)) return false;
		}
		
		return true;
	}
	
	

	
	public static PointResponseDTO toPointDTO(String coordinates) {
		PointResponseDTO pointDTO = new PointResponseDTO();
		ResultMessageDTO resultMessageDTO = new ResultMessageDTO();
		
		String[] stringCoordinates = coordinates.split(",");
		
		if(!arePointCoordinatesValid(coordinates)) {
			pointDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return pointDTO;
		}
		
		Double lon = Double.parseDouble(stringCoordinates[0]); 
		Double lat = Double.parseDouble(stringCoordinates[1]);	
	
		pointDTO.setLon(lon);
		pointDTO.setLat(lat);
		pointDTO.setResultMessage(resultMessageDTO);
		
		return pointDTO;
	}

	//VIENE UTILIZZATA?
	/*
	public static List<PointResponseDTO> toListPointDTO(String coordinates) {
		
		String[] stringPoints = coordinates.split(";");

		List<PointResponseDTO> pointsDTO = new ArrayList<PointResponseDTO>();
		PointResponseDTO pointDTO;
		ResultMessageDTO resultMessageDTO;
		for(String stringPoint: stringPoints) {
			pointDTO = toPointDTO(stringPoint);
			
			resultMessageDTO = pointDTO.getResultMessage();
			if(resultMessageDTO.getCode() != ResultCodeUtils.SUCCESS_CODE) {
				return null;
			}
			
			pointsDTO.add(pointDTO);
		}
		
		return pointsDTO;
	}
	*/
	
	
	
	
	
	
	
}
