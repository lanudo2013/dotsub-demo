package dotsub.demo.test;

import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dotsub.demo.config.DatabaseTestConfig;
import dotsub.demo.config.TestConfig;
import dotsub.demo.endpoint.api.FileDTO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DatabaseTestConfig.class, TestConfig.class})
public class FilePersisterTest extends AbstractTest {
	
	@BeforeEach
	public void setup() throws IOException {
		fileManagerMockSetup();
		repository.deleteAll();
	}
	
	@Test
	public void tryToSaveFileWithEmptyTitleOrDate() {
		
		try {
			var f = new FileDTO(null, null, "", new Date(), 180);
			f.setFile(new MockMultipartFile("default.pdf", new byte[] {1}));
			service.saveFile(f);
			Assertions.fail();
		} catch(Exception e){
		}
		
		try {
			FileDTO f = new FileDTO(null, "1234", "1234", null, null);
			f.setFile(new MockMultipartFile("default.pdf", new byte[] {1}));
			service.saveFile(f);
			Assertions.fail();
		} catch(Exception e){
		}
				
		
	}
	
	
	@Test
	public void tryToSaveFileWithoutFileInstance() {
		
		try {
			var f = new FileDTO(null, "1234", "1234", new Date(), 180);
			service.saveFile(f);
			Assertions.fail();
		} catch(Exception e){
		}
		
	}
	
	
	@Test
	public void saveValidFileAndRetrievedFromDB() {
		final var title = "title1";
		final var fileName = "my-file.pdf";
		final var data = new byte[] {1, 1, 5};
		try {
			var f = new FileDTO(null, title, "1234", new Date(), 180);
			var file = new MockMultipartFile(fileName, fileName, "application/pdf", data);
			f.setFile(file);
			service.saveFile(f);
			var fileInstance = service.getFiles().get(0);
			Assertions.assertEquals(f.getTitle(), fileInstance.getTitle());
			Assertions.assertEquals("pdf", fileInstance.getExtension());
			Assertions.assertEquals(data.length, fileInstance.getSize().intValue());
			var bytes = service.readFile(fileInstance.getId());
			Assertions.assertArrayEquals(bytes.getData(), data);
		} catch(Exception e){
			Assertions.fail(e);
		}
		
	}
	
	@Test
	public void saveManyValidFilesAndRetrieveThem() {
		final var ttitle = "title";
		final var ffilename = "my-file";	
		final var extension = "pdf";
		final var byteArray = new byte[][] {
			new byte[] {1, 5, 0, 2},
			new byte[] {2, 5, 10, 2, 8, 9, 1},
			new byte[] {5, 5, 5},
			new byte[] {1, 3},
		};
		
		try {
			for (int i = 0; i < byteArray.length; i++) {
				var f = new FileDTO(null, ttitle + i, "1234", new Date(), 180);
				var file = new MockMultipartFile(ffilename + i + "." + extension, ffilename + i + "." + extension, "application/pdf", byteArray[i]);
				f.setFile(file);
				service.saveFile(f);
			}
			
			for (int i = 0; i < byteArray.length; i++) {
				var fileInstance = service.getFiles().get(i);
				var bytes = service.readFile(fileInstance.getId());
				Assertions.assertArrayEquals(bytes.getData(), byteArray[i]);
			}

		} catch(Exception e){
			Assertions.fail(e);
		}
		
	}
	
	
	
	
}
