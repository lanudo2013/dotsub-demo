package dotsub.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FilePersisterService {

	void saveFile(String fileId, MultipartFile multipart) throws IOException;

}
