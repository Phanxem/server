package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.RouteDTO;
import com.natour.server.application.dtos.PointDTO;

import java.util.List;



public interface RouteDAO {

	RouteDTO findRouteByCoordinates(String coordinates);
	
	RouteDTO findRouteByPoints(List<PointDTO> points);
	
}
