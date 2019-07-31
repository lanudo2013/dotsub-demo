package dotsub.demo.service;

import java.io.IOException;
import java.util.List;

import dotsub.demo.endpoint.api.FileDTO;
import dotsub.demo.endpoint.api.FileMetadataDTO;
import dotsub.demo.endpoint.api.ServedFileDTO;
import dotsub.demo.exception.AppException;

public interface DotsubService {
	void saveFile(FileDTO params) throws Exception;

	List<FileMetadataDTO> getFiles();
	
	ServedFileDTO readFile(String id) throws AppException, IOException;
}
