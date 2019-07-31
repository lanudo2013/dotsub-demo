package dotsub.demo.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="file")
public class DotsubFile {

	@Id
	@Column(name="id", nullable=false)
	@GenericGenerator(name = "uuid_custom_sequence", strategy = "dotsub.demo.model.IdGenerator")
	@GeneratedValue (generator = "uuid_custom_sequence")
	private String id;
	
	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="description", nullable=true)
	private String description;
	
	@Column(name="extension", nullable=false)
	private String extension;
	
	@Column(name="mimeType", nullable=true)
	private String mimeType;
	
	@Column(name="size", nullable=false)
	private BigInteger size;
	
	@Column(name="creationDate", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Column(name="timeOffset", nullable=false)
	private Integer timeOffset;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
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

	public BigInteger getSize() {
		return size;
	}

	public void setSize(final BigInteger size) {
		this.size = size;
	}
	
}
