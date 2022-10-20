package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.response.GetResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.StringResponseDTO;

public interface ImageDAO {

	public GetResourceResponseDTO getByName(String name);
	
	public StringResponseDTO put(String name, byte[] image);
	
	public ResultMessageDTO delete (String name);
}
