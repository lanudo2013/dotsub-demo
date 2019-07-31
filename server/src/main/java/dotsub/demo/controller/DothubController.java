package dotsub.demo.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dotsub.demo.exception.AppException;
import dotsub.demo.service.DotsubService;
import io.swagger.annotations.ApiOperation;

@Controller
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public class DothubController {
	
	@Autowired
	private DotsubService service;

	@RequestMapping(path = "/files/download/{id}", method=RequestMethod.GET)
	@ApiOperation(
			notes = "Endpoint to download a file by an id and returns an attachment", 
			value = "Endpoint to download a file by an id",
			response = HttpServletResponse.class,
			httpMethod = "GET",
			nickname = "getFile")
	public void getFile(@PathVariable("id") @NotEmpty final String id, final HttpServletRequest request,
							final HttpServletResponse response) throws IOException, AppException {
        final var dto = service.readFile(id);
        
        response.setContentType(dto.getMetadata().getMimeType());
        final var ext = dto.getMetadata().getExtension();
        response.setHeader("Content-disposition", "attachment; filename=" + dto.getMetadata().getTitle() + 
        					(ext != null ? String.format(".%s", ext) : ""));
 
        try(final OutputStream out = response.getOutputStream()) {
        	final var size = dto.getMetadata().getSize().intValue();
        	final var buffer = dto.getData();
            out.write(buffer, 0, size);
        }
    }
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }
}
