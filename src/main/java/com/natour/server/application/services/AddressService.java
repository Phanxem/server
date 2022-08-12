package com.natour.server.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natour.server.CoordinatesUtils;
import com.natour.server.application.dtos.AddressDTO;
import com.natour.server.application.dtos.PointDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressCoordinatesInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.AddressNotFoundException;
import com.natour.server.data.dao.implemented.AddressDAOImpl;
import com.natour.server.data.dao.interfaces.AddressDAO;


@Service
public class AddressService {

	//TODO test
	AddressDAO addressDAO = new AddressDAOImpl();
	
	//FINDs
	public AddressDTO findAddressByCoordinates(String coordinates) {		
		
		PointDTO pointDTO = CoordinatesUtils.toPointDTO(coordinates);
		System.out.println("before dao");
		AddressDTO result = addressDAO.findAddressByPoint(pointDTO);
		System.out.println("after dao");
		if(result == null) throw new AddressNotFoundException();
	
		return result;
	}

	//SEARCHs
	public List<AddressDTO> searchAddressesByQuery(String query) {		
		
		List<AddressDTO> results = addressDAO.findAddressesByQuery(query);
		
		//if(results == null) throw new AddressNotFoundException();
	
		return results;
	}
	
	
}
