package com.natour.server.data.dao.implemented;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.natour.server.application.dtos.response.GetResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.StringResponseDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.interfaces.GpxDAO;

@Component
public class GpxDAOImpl implements GpxDAO{

	//private final static String BUCKET_NAME = "natour-gpxs";
	private final static String BUCKET_NAME = "natour-itinerary-gpx";
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Override
	public GetResourceResponseDTO getByName(String name) {
		GetResourceResponseDTO resourceResponseDTO = new GetResourceResponseDTO();
		
		S3Object s3object = null;
		try {
			s3object = amazonS3.getObject(BUCKET_NAME,name);
		}
		catch(Exception e) {
			resourceResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
			return resourceResponseDTO;
		}
				
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		Resource resource = new InputStreamResource(inputStream);
		
		resourceResponseDTO.setResource(resource);
		resourceResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return resourceResponseDTO;
	}

	
	@Override
	public StringResponseDTO put(String name, byte[] gpx) {
		StringResponseDTO stringResponseDTO = new StringResponseDTO();
		
		String completeName = new Date().getTime() + "-gpx-" + name;
		
		File file = new File("." + File.separator + completeName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, false);
		}
		catch (IOException e) {	
			stringResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
			return stringResponseDTO;
		}
		
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(gpx);
			fileOutputStream.flush();
			fileOutputStream.close();	
		}
		catch (FileNotFoundException e) {
			stringResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return stringResponseDTO;
		}
		catch (IOException e) {
			stringResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
			return stringResponseDTO;
		}
		
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, completeName, file);
		
		try {
			amazonS3.putObject(putObjectRequest);
		}
		catch(Exception e) {
			stringResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
			return stringResponseDTO;
		}
        
		stringResponseDTO.setString(completeName);
		stringResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return stringResponseDTO;
	}

	@Override
	public ResultMessageDTO delete(String name) {
		
		try {
			amazonS3.deleteObject(BUCKET_NAME,name);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
			
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}

}
