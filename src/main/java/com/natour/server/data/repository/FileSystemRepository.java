package com.natour.server.data.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepository {

	String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

	public String save(String fileName, byte[] content) throws IOException {
		Path path = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + fileName);
	      
		Files.createDirectories(path.getParent());
		Files.write(path, content);

		return path.toAbsolutePath().toString();
	}
	
	public FileSystemResource findInFileSystem(String location) {
		return new FileSystemResource(Paths.get(location));
	}
	
	//TODO DA TESTARE
	public void delete(String stringPath) throws IOException {
		Path path = Paths.get(stringPath);
		Files.delete(path);
	}
	
}
