package dotsub.demo.endpoint.api;

public class ServedFileDTO {

	private byte[] data;
	private FileMetadataDTO metadata;	

	public ServedFileDTO(final byte[] data, final FileMetadataDTO metadata) {
		super();
		this.data = data.clone();
		this.metadata = metadata;
	}
	public byte[] getData() {
		return data.clone();
	}
	public void setData(final byte[] data) {
		this.data = data.clone();
	}
	public FileMetadataDTO getMetadata() {
		return metadata;
	}
	public void setMetadata(final FileMetadataDTO metadata) {
		this.metadata = metadata;
	}
	
}
