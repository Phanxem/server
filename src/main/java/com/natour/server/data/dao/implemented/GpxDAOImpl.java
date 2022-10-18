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
import com.natour.server.application.dtos.response.ResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.StringResponseDTO;
import com.natour.server.data.dao.interfaces.GpxDAO;

@Component
public class GpxDAOImpl implements GpxDAO{

	private final static String BUCKET_NAME = "natour-gpxs";
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Override
	public ResourceResponseDTO getByName(String name) {
		
		ResourceResponseDTO resourceResponseDTO = new ResourceResponseDTO();
		
		S3Object s3object = null;
		try {
			s3object = amazonS3.getObject(BUCKET_NAME,name);
		}
		catch(Exception e) {
			//TODO
			return null;
		}
				
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		Resource resource = new InputStreamResource(inputStream);
		
		resourceResponseDTO.setResource(resource);
		resourceResponseDTO.setResultMessage(new ResultMessageDTO());
		
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(gpx);
			fileOutputStream.flush();
			fileOutputStream.close();	
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, completeName, file);
		
		try {
			amazonS3.putObject(putObjectRequest);
		}
		catch(Exception e) {
			//TODO
			return null;
		}
        
		stringResponseDTO.setString(completeName);
		stringResponseDTO.setResultMessage(new ResultMessageDTO());
		
		return stringResponseDTO;
	}

	@Override
	public ResultMessageDTO delete(String name) {
	
		ResultMessageDTO resultMessageDTO = new ResultMessageDTO();
		
		try {
			amazonS3.deleteObject(BUCKET_NAME,name);
		}
		catch(Exception e) {
			//TODO
			return null;
		}
			
		return resultMessageDTO;
	}

}
