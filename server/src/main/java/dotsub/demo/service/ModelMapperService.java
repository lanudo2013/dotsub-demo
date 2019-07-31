package dotsub.demo.service;

import dotsub.demo.endpoint.api.FileDTO;
import dotsub.demo.endpoint.api.FileMetadataDTO;
import dotsub.demo.model.DotsubFile;

public interface ModelMapperService {

	DotsubFile fromFileDTOToEntity(FileDTO dto);

	FileMetadataDTO fromEntityToMetadataDTO(DotsubFile entity);

}
