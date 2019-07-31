package dotsub.demo.service.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dotsub.demo.service.FileManagerService;

@Service
public class FileManagerServiceBean implements FileManagerService {
	private static final String FILE_DIR = "./files";
	
	private Path getFilePath(final String fileId, final String extension) {
		return Paths.get(FILE_DIR, fileId + (!extension.isBlank() ? "." + extension : ""));
	}
	
	@Override
	public Path saveFile(final String fileId, final MultipartFile multipart) throws IOException {
		synchronized (FILE_DIR) {
			if (!Files.isDirectory(Paths.get(FILE_DIR))) {
				Files.createDirectory(Paths.get(FILE_DIR));
			}
		}
		final var fileName = multipart.getOriginalFilename();
		final var extension = FilenameUtils.getExtension(fileName);
		final var result = Files.write(getFilePath(fileId, extension), multipart.getBytes());
		return result.getFileName(); 
	}

	@Override
	public byte[] readFileData(final String fileId, final String extension) throws IOException {
		return Files.readAllBytes(getFilePath(fileId, extension));
	}
}
