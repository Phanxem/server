package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.dtos.response.RouteResponseDTO;

import java.util.List;



public interface RouteDAO {

	RouteResponseDTO findRouteByCoordinates(String coordinates);
	
}
