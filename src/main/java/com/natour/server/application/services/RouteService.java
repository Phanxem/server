package com.natour.server.application.services;



import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.response.RouteResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.RouteCoordinatesInvalidException;
import com.natour.server.application.services.utils.CoordinatesUtils;
import com.natour.server.data.dao.implemented.RouteDAOImpl;
import com.natour.server.data.dao.interfaces.RouteDAO;

@Service
public class RouteService {

	//TODO test
	RouteDAO routeDAO = new RouteDAOImpl();
		
	//FINDs
	public RouteResponseDTO findRouteByCoordinates(String coordinates) {		
			
		if(!CoordinatesUtils.areRouteCoordinatesValid(coordinates)) {
			//TODO
			throw new RouteCoordinatesInvalidException();
		}
			
		RouteResponseDTO result = routeDAO.findRouteByCoordinates(coordinates);
		
		return result;
	}
		
		
		
}
