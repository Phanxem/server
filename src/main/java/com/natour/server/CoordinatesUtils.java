package com.natour.server;

import java.util.ArrayList;
import java.util.List;

import com.natour.server.application.dtos.PointDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressCoordinatesInvalidException;


public class CoordinatesUtils {

	public final static double MIN_LON = -180;
	public final static double MAX_LON = 180;
	public final static double MIN_LAT = -90;
	public final static double MAX_LAT = 90;
	
	
	public static boolean arePointCoordinatesValid(String coordinates) {
		
		String[] stringCoordinates = coordinates.split(",");
		
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
	
	
	/*
	public static PointDTO toPointDTO(String coordinates) {
		
		
		
		String[] stringCoordinates = coordinates.split(",");
		
		Double lon = Double.parseDouble(stringCoordinates[0]); 
		Double lat = Double.parseDouble(stringCoordinates[1]);

				
		PointDTO pointDTO = new PointDTO(lon, lat);
		
		return pointDTO;
	}
	*/
	
	public static PointDTO toPointDTO(String coordinates) {
		
		String[] stringCoordinates = coordinates.split(",");
		
		if(stringCoordinates.length < 2) {
			throw new AddressCoordinatesInvalidException();
		}
		
		Double lon = null;
		Double lat = null;
		try {
			lon = Double.parseDouble(stringCoordinates[0]); 
			lat = Double.parseDouble(stringCoordinates[1]);	
		}
		catch(NumberFormatException e) {
			throw new AddressCoordinatesInvalidException();
		}
		
		if(lon < MIN_LON ||
		   lon > MAX_LON ||
		   lat < MIN_LAT ||
		   lat > MAX_LAT )
		{
			throw new AddressCoordinatesInvalidException();
		}
	
		PointDTO pointDTO = new PointDTO(lon, lat);
		
		return pointDTO;
	}
	
	
	public static List<PointDTO> toListPointDTO(String coordinates) {
		
		String[] stringPoints = coordinates.split(";");

		List<PointDTO> pointsDTO = new ArrayList<PointDTO>();
		PointDTO pointDTO;
		
		for(String stringPoint: stringPoints) {
			pointDTO = toPointDTO(stringPoint);
			pointsDTO.add(pointDTO);
		}
		
		return pointsDTO;
	}
	
	
	
	
	
	
	
	
}
