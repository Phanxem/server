package com.natour.server.data.dao.interfaces;

import java.util.List;

import com.natour.server.application.dtos.AddressDTO;
import com.natour.server.application.dtos.PointDTO;

public interface AddressDAO {

	AddressDTO findAddressByPoint(PointDTO point);
	
	List<AddressDTO> findAddressesByQuery(String query);
	
}
