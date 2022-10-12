package com.natour.server.application.services.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.FileSystemResource;

import com.natour.server.application.exceptionHandler.serverExceptions.FileConvertionFailureException;

public class FileSystemUtils {

	//CONVETERs
		public static byte[] toArrayByte(FileSystemResource fileSystemResource) {
			File file = fileSystemResource.getFile();
			
			byte[] result;
			try {
				result = Files.readAllBytes(file.toPath());
			} 
			catch (IOException e) {	
				throw new FileConvertionFailureException();		
			}
			
			return result;
		}
}
