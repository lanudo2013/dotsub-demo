package dotsub.demo.service.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dotsub.demo.service.FilePersisterService;

@Service
public class FilePersisterServiceBean implements FilePersisterService {
	private static final String FILE_DIR = "./files";
	
	@Override
	public void saveFile(final String fileId, final MultipartFile multipart) throws IOException {
		synchronized (FILE_DIR) {
			if (!Files.isDirectory(Paths.get(FILE_DIR))) {
				Files.createDirectory(Paths.get(FILE_DIR));
			}
		}
		final var fileName = multipart.getName();
		final var extension = FilenameUtils.getExtension(fileName);
		Files.write(Paths.get(FILE_DIR, fileId + (!extension.isBlank() ? "." + extension : "")), multipart.getBytes());
	}
}
