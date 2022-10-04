package com.natour.server.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natour.server.CoordinatesUtils;
import com.natour.server.application.dtos.response.AddressResponseDTO;
import com.natour.server.application.dtos.response.ListAddressResponseDTO;
import com.natour.server.application.dtos.response.PointResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressCoordinatesInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressNotFoundException;
import com.natour.server.data.dao.implemented.AddressDAOImpl;
import com.natour.server.data.dao.interfaces.AddressDAO;


@Service
public class AddressService {

	//TODO test
	AddressDAO addressDAO = new AddressDAOImpl();
	
	//FINDs
	public AddressResponseDTO findAddressByCoordinates(String coordinates) {		
		
		PointResponseDTO pointDTO = CoordinatesUtils.toPointDTO(coordinates);
		AddressResponseDTO result = addressDAO.findAddressByPoint(pointDTO);

		return result;
	}

	//SEARCHs
	public ListAddressResponseDTO searchAddressesByQuery(String query) {		
		
		ListAddressResponseDTO results = addressDAO.findAddressesByQuery(query);
		
		return results;
	}
	
	
}
