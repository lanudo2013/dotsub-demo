package dotsub.demo.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import dotsub.demo.config.WebConfig;
import dotsub.demo.endpoint.api.FileDTO;
import dotsub.demo.endpoint.api.FileMetadataDTO;
import dotsub.demo.endpoint.api.ResponseDTO;
import dotsub.demo.exception.AppException;
import dotsub.demo.service.DotsubService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/files")
@RestControllerAdvice
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
public class DothubEndpoint {
	
	private static final Logger LOG = Logger.getLogger(DothubEndpoint.class);
	private static final String appJsonResponseType = "application/json";
	
	@Autowired
	private DotsubService service;
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public final ResponseEntity<ResponseDTO<Integer>> handleMaxUploadSizeExceeded
												(final MaxUploadSizeExceededException ex, final WebRequest request) {
		final var response = new ResponseDTO<Integer>();
		response.setErrorMessage("request.error.maxSize");
		response.setData(WebConfig.getMaxFileSize());
		final var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<ResponseDTO<Integer>>(response, headers, HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(path = "/create", method=RequestMethod.POST, produces="application/json", 
			consumes = {"multipart/form-data"})
	@ApiOperation(
			notes = "Endpoint to store all file information: title, description, creation date and file instance", 
			value = "Endpoint to store all file information: title, description, creation date and file instance",
			consumes = "multipart/form-data",
			produces = appJsonResponseType,
			httpMethod = "POST",
			nickname = "createFile")
	public ResponseEntity<ResponseDTO<Void>> createFile( @ModelAttribute @Valid final FileDTO params){
		
		final var response = new ResponseDTO<Void>();
		
		try{
			service.saveFile(params);
			response.setSuccess(true);
			return new ResponseEntity<ResponseDTO<Void>>(response, HttpStatus.OK);
		} catch (AppException e) {
			response.setErrorMessage(e.getMessage());
			LOG.error("Unable to save file", e);
			return new ResponseEntity<ResponseDTO<Void>>(response, HttpStatus.BAD_REQUEST);
		}
		catch(Exception e){
			response.setErrorMessage(e.getMessage());
			LOG.error("Unable to save file", e);
			return new ResponseEntity<ResponseDTO<Void>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	@ApiOperation(
			notes = "Endpoint to retrieve all files saved ", 
			value = "Endpoint to retrieve all files saved",
			produces = appJsonResponseType,
			httpMethod = "GET",
			nickname = "getFiles")
	@ApiResponse(response = FileMetadataDTO.class, responseContainer = "List", message = "The list of all files metadata", code = 200)
	public ResponseEntity<ResponseDTO<List<FileMetadataDTO>>> getFiles(){
		final var response = new ResponseDTO<List<FileMetadataDTO>>();
		
		try{
			response.setData(service.getFiles());
			response.setSuccess(true);
			return new ResponseEntity<ResponseDTO<List<FileMetadataDTO>>>(response, HttpStatus.OK);
		} 
		catch(Exception e){
			response.setErrorMessage(e.getMessage());
			LOG.error("Unable to read files", e);
			return new ResponseEntity<ResponseDTO<List<FileMetadataDTO>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
