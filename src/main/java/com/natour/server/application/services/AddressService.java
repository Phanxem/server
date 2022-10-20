package com.natour.server.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.response.GetAddressResponseDTO;
import com.natour.server.application.dtos.response.GetListAddressResponseDTO;
import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.services.utils.CoordinatesUtils;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.implemented.AddressDAOImpl;
import com.natour.server.data.dao.interfaces.AddressDAO;


@Service
public class AddressService {

	//TODO test
	AddressDAO addressDAO = new AddressDAOImpl();
	
	//FINDs
	public GetAddressResponseDTO findAddressByCoordinates(String coordinates) {		
		GetAddressResponseDTO addressResponseDTO = new GetAddressResponseDTO();
		
		PointResponseDTO pointDTO = CoordinatesUtils.toPointDTO(coordinates);
		if(!ResultMessageUtils.isSuccess(pointDTO.getResultMessage())) {
			addressResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
		}
		
		addressResponseDTO = addressDAO.findAddressByPoint(pointDTO);

		return addressResponseDTO;
	}

	//SEARCHs
	public GetListAddressResponseDTO searchAddressesByQuery(String query) {		
		
		GetListAddressResponseDTO listAddressResponseDTO = addressDAO.findAddressesByQuery(query);
		
		return listAddressResponseDTO;
	}
	
	
}
