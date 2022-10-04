package com.natour.server.data.dao.interfaces;

import java.util.List;

import com.natour.server.application.dtos.response.AddressResponseDTO;
import com.natour.server.application.dtos.response.ListAddressResponseDTO;
import com.natour.server.application.dtos.response.PointResponseDTO;

public interface AddressDAO {

	AddressResponseDTO findAddressByPoint(PointResponseDTO point);
	
	ListAddressResponseDTO findAddressesByQuery(String query);
	
}
