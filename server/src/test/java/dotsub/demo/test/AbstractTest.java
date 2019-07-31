package dotsub.demo.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import dotsub.demo.repo.DotsubFileRepo;
import dotsub.demo.service.DotsubService;
import dotsub.demo.service.FileManagerService;


public abstract class AbstractTest {
	
	@Autowired
	protected DotsubService service;
	@Autowired
	@MockBean
	protected FileManagerService fileManager;
	@Autowired
	protected DotsubFileRepo repository;
	
	protected Map<String, byte[]> fileInfo = new HashMap<>(); 

	protected void fileManagerMockSetup() throws IOException {
		
		Mockito.when(fileManager.saveFile(Mockito.anyString(), Mockito.any())).thenAnswer(i -> {
			fileInfo.put(i.getArgument(0), ((MultipartFile)i.getArgument(1)).getBytes());
			return null;
		});
		
		Mockito.when(fileManager.readFileData(Mockito.anyString(), Mockito.anyString()))
			.thenAnswer(new Answer<byte[]>() {
				@Override
				public byte[] answer(InvocationOnMock i) throws Throwable {
					
					if (!fileInfo.containsKey(i.getArgument(0))) {
						throw new Exception();
					}
					return fileInfo.get(i.getArgument(0));
				}
			});
	}
}
