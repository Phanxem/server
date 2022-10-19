package com.natour.server.application.services;



import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.response.RouteResponseDTO;
import com.natour.server.application.services.utils.CoordinatesUtils;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.implemented.RouteDAOImpl;
import com.natour.server.data.dao.interfaces.RouteDAO;

@Service
public class RouteService {

	//TODO test
	RouteDAO routeDAO = new RouteDAOImpl();
		
	//FINDs
	public RouteResponseDTO findRouteByCoordinates(String coordinates) {		
		RouteResponseDTO routeResponseDTO = new RouteResponseDTO();	
		
		
		if(!CoordinatesUtils.areRouteCoordinatesValid(coordinates)) {
			routeResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return routeResponseDTO;
		}
			
		routeResponseDTO = routeDAO.findRouteByCoordinates(coordinates);
		
		return routeResponseDTO;
	}
		
		
		
}
