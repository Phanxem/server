package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.dtos.response.GetRouteResponseDTO;

import java.util.List;



public interface RouteDAO {

	GetRouteResponseDTO findRouteByCoordinates(String coordinates);
	
}
