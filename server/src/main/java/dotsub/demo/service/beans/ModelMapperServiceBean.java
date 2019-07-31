package dotsub.demo.service.beans;

import org.springframework.stereotype.Service;

import dotsub.demo.endpoint.api.FileDTO;
import dotsub.demo.endpoint.api.FileMetadataDTO;
import dotsub.demo.model.DotsubFile;
import dotsub.demo.service.ModelMapperService;

@Service
public class ModelMapperServiceBean implements ModelMapperService {

	@Override
	public DotsubFile fromFileDTOToEntity(final FileDTO dto) {
		final var entity = new DotsubFile();
		entity.setCreationDate(dto.getCreationDate());
		entity.setDescription(dto.getDescription());
		entity.setTitle(dto.getTitle());
		entity.setTimeOffset(dto.getTimeOffset());
		return entity;
	}
	
	@Override
	public FileMetadataDTO fromEntityToMetadataDTO(final DotsubFile entity) {
		return new FileMetadataDTO(entity.getId(), entity.getTitle(), entity.getDescription(), 
									entity.getCreationDate(), entity.getTimeOffset(), entity.getSize().longValue(), 
									entity.getExtension(), entity.getMimeType());		
	}
	
}
