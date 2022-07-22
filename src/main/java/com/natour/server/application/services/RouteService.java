package com.natour.server.application.services;



import org.springframework.stereotype.Service;

import com.natour.server.CoordinatesUtils;
import com.natour.server.application.dtos.RouteDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.RouteCoordinatesInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.RouteNotFoundException;
import com.natour.server.data.dao.implemented.RouteDAOImpl;
import com.natour.server.data.dao.interfaces.RouteDAO;

@Service
public class RouteService {

	//TODO test
	RouteDAO routeDAO = new RouteDAOImpl();
		
	//FINDs
	public RouteDTO findRouteByCoordinates(String coordinates) {		
			
		if(!CoordinatesUtils.areRouteCoordinatesValid(coordinates)) throw new RouteCoordinatesInvalidException();
		
		//List<PointDTO> pointsDTO = CoordinatesUtils.toListPointDTO(coordinates);
			
		RouteDTO result = routeDAO.findRouteByCoordinates(coordinates);
			
		if(result == null) throw new RouteNotFoundException();
		
		return result;
	}
		
		
		
}
