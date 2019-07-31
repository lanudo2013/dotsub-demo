package dotsub.demo.endpoint.api;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class FileMetadataDTO extends FileDTO {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name = "size", value = "File size in KBs", required = true, position = 7, example = "200")
	private Long size;
	@ApiModelProperty(name = "extension", value = "File extension", required = true, position = 8, example = "pdf")
	private String extension;
	@ApiModelProperty(name = "mimeType", value = "File type", required = true, position = 9, example = "application/pdf")
	private String mimeType;
	
	
	public FileMetadataDTO(final String id, final String title, final String description, final Date creationDate, final Integer timeOffset, final Long size, final String extension, final String mimeType) {
		super(id, title, description, creationDate, timeOffset);
		this.size = size;
		this.extension = extension;
		this.mimeType = mimeType;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(final Long size) {
		this.size = size;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(final String extension) {
		this.extension = extension;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}
	
	
}
