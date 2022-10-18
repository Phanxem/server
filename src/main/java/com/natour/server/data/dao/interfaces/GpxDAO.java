package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.response.ResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.StringResponseDTO;

public interface GpxDAO {

	public ResourceResponseDTO getByName(String name);
	
	public StringResponseDTO put(String name, byte[] gpx);
	
	public ResultMessageDTO delete (String name);
	
}
