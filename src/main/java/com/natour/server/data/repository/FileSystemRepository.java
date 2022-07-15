package com.natour.server.data.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepository {

	String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

	public String save(String imageName, byte[] content) throws Exception {
	        Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + imageName);
	        Files.createDirectories(newFile.getParent());

	        Files.write(newFile, content);

	        return newFile.toAbsolutePath().toString();
	}
	
	public FileSystemResource findInFileSystem(String location) {
	    try {
	        return new FileSystemResource(Paths.get(location));
	    } 
	    catch (Exception e) {
	        // Handle access or file not found problems.
	        throw new RuntimeException();
	    }
	}
	
}
