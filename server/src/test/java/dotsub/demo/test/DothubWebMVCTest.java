package dotsub.demo.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dotsub.demo.config.DatabaseTestConfig;
import dotsub.demo.config.TestConfig;
import dotsub.demo.config.WebTestConfig;
import dotsub.demo.endpoint.api.FileDTO;
import dotsub.demo.endpoint.api.ResponseDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@ContextConfiguration(classes = { DatabaseTestConfig.class, TestConfig.class, WebTestConfig.class})
public class DothubWebMVCTest extends AbstractTest {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@BeforeEach
	public void init() throws IOException {
		fileManagerMockSetup();
	}
	
	@Test
	@WithMockUser
	public void tryToPostObjectWithEmptyFile() throws Exception {
		var f = new FileDTO(null, "HelloWorld", null, new Date(), 240);
		var sdf = new SimpleDateFormat("yyyy-MM-dd");
	    mvc.perform(MockMvcRequestBuilders.multipart("/files/create")
			    		.param("title", f.getTitle())
						.param("creationDate", sdf.format(f.getCreationDate()))
						.param("timeOffset", f.getTimeOffset().toString())
	    			 	.with(SecurityMockMvcRequestPostProcessors.csrf())
	    			)
	      .			andExpect(MockMvcResultMatchers.status().isBadRequest()
	    		  );
	}
	
	@Test
	@WithMockUser
	public void tryToPostObjectWithMissingDate() throws Exception {

		var f = new FileDTO(null, "HelloWorld", null, null, null);
	    mvc.perform(MockMvcRequestBuilders.multipart("/files/create")
	    				.file(createSampleFile())
			    		.param("title", f.getTitle())
	    			 	.with(SecurityMockMvcRequestPostProcessors.csrf())
	    				.contentType(MediaType.MULTIPART_FORM_DATA)
	    			)
	      .			andExpect(MockMvcResultMatchers.status().isBadRequest()
	    		  );
	}
	
	@Test
	@WithMockUser
	public void tryToSaveValidFileAndRetrieveItFromServer() throws Exception {

		final var title = "json-example";
		
		var cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 30);
		var sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		var f = new FileDTO(null, title, null, cal.getTime(), 90);		
		final var creationDateStr = sdf.format(f.getCreationDate());
		
		mvc.perform(MockMvcRequestBuilders
	    				.multipart("/files/create")
	    				.file(createSampleFile())
	    				.param("title", f.getTitle())
	    				.param("creationDate", creationDateStr)
	    				.param("timeOffset", f.getTimeOffset().toString())
	    				.contentType(MediaType.MULTIPART_FORM_DATA)
	    			 	.with(SecurityMockMvcRequestPostProcessors.csrf())
	    			)
	      .			andExpect(MockMvcResultMatchers.status().isOk()
	    		  );
		
		var result = mvc.perform(MockMvcRequestBuilders
				.get("/files")
			 	.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
  .			andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		ResponseDTO<List<Map<String, Object>>> payload = objectMapper.readerFor(ResponseDTO.class)
															.readValue(result.getResponse().getContentAsString());
		Assertions.assertEquals(payload.getData().size(), 1);
		var list = payload.getData();
		var file = list.get(0);
		Assertions.assertEquals(file.get("title"), title);
		Assertions.assertEquals(file.get("creationDate"), creationDateStr);
	}
	

	private MockMultipartFile createSampleFile() {
		return new MockMultipartFile("file", "my-json-file.json", "application/json", "{\"json\": \"someValue\"}".getBytes());
	}
}
