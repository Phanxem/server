package com.natour.server.application.services.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.jenetics.jpx.GPX;

public class ByteConverterUtils {

	//CONVERTERs
	public byte[] toArrayByte(GPX gpx, String fileName) {
		File file = new File(fileName + ".gpx");
		byte[] gpxByte;
		
		try {
			GPX.writer().write(gpx, file);
			gpxByte = Files.readAllBytes(file.toPath());
		}
		catch (IOException e) {
			return null;
		}
		
		return gpxByte;
	}
	
}
