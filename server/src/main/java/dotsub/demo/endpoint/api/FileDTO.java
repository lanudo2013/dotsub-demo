package dotsub.demo.endpoint.api;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class FileDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Null
	@ApiModelProperty(name = "id", hidden = true)
	private String id;
	@NotEmpty
	@Pattern(regexp = "^([a-zA-Z0-9 _-]+)$")
	@Size(min = 2, max = 50)
	@ApiModelProperty(name = "title", value = "The title for the new file", required = true, position = 1)
	private String title;
	@ApiModelProperty(name = "description", value = "Description for the new file", required = false, position = 2)
	@Size(min = 3, max = 200)
	private String description;
	@NotNull
	@ApiModelProperty(name = "file", value = "File instance with all file information", required = true, position = 5)
	private MultipartFile file;
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(name = "creationDate", value = "Creation date for the new file", required = true, position = 3, example = "2017-01-01")
	private Date creationDate;
	@NotNull
	@ApiModelProperty(name = "timeOffset", value = "Time offset of the creation date", required = true, position = 4)
	private Integer timeOffset;	
	
	/**
	 * Default constructor
	 */
	public FileDTO() {
	}
	
	public FileDTO(final String id, final String title, final String description, final Date creationDate,final Integer timeOffset) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.creationDate = creationDate;
		this.timeOffset = timeOffset;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(final MultipartFile file) {
		this.file = file;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(final Integer timeOffset) {
		this.timeOffset = timeOffset;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}
}
