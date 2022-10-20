package com.natour.server.data.dao.interfaces;

import java.util.List;

import com.natour.server.application.dtos.response.GetAddressResponseDTO;
import com.natour.server.application.dtos.response.GetListAddressResponseDTO;
import com.natour.server.application.dtos.response.PointResponseDTO;

public interface AddressDAO {

	GetAddressResponseDTO findAddressByPoint(PointResponseDTO point);
	
	GetListAddressResponseDTO findAddressesByQuery(String query);
	
}
