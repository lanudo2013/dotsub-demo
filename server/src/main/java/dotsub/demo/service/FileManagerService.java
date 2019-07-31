package dotsub.demo.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileManagerService {

	Path saveFile(String fileId, MultipartFile multipart) throws IOException;
	
	byte[] readFileData(String id, String extension) throws IOException;

}
