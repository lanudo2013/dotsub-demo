package dotsub.demo.service.beans;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import dotsub.demo.endpoint.api.FileDTO;
import dotsub.demo.endpoint.api.FileMetadataDTO;
import dotsub.demo.endpoint.api.ServedFileDTO;
import dotsub.demo.exception.AppException;
import dotsub.demo.repo.DotsubFileRepo;
import dotsub.demo.service.DotsubService;
import dotsub.demo.service.FileManagerService;
import dotsub.demo.service.ModelMapperService;

@Service
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class DotsubServiceBean implements DotsubService {
	
	@Autowired
	private DotsubFileRepo repository;
	
	@Autowired
	private FileManagerService fileManager;
	
	@Autowired
    private Validator validator;
	
	@Autowired
	private ModelMapperService mapper;
	
	@Override
	@Transactional
	public void saveFile(final FileDTO params) throws Exception {
		validateRequiredParams(params);
		validateDate(params);
		var entity = mapper.fromFileDTOToEntity(params);
		final var extension = FilenameUtils.getExtension(params.getFile().getOriginalFilename());
		entity.setMimeType(params.getFile().getContentType());
		entity.setSize(BigInteger.valueOf(params.getFile().getSize()));
		if (!extension.isBlank()) {
			entity.setExtension(extension);
		}
		entity = repository.save(entity);
		fileManager.saveFile(entity.getId(), params.getFile());
		
	}
	
	
	private void validateRequiredParams(final FileDTO params) throws AppException {
		final var errors = new BeanPropertyBindingResult(params, params.getClass().getName());
		validator.validate(params, errors);
		if (errors != null && errors.hasErrors()) {
			throw new AppException("Invalid model");
		}        
	}

	private void validateDate(final FileDTO params) throws AppException {
		final var creationDate = LocalDate.ofInstant(params.getCreationDate().toInstant(),
								ZoneOffset.ofTotalSeconds(-params.getTimeOffset() * 60).normalized());
		final var topToday = LocalDate.ofInstant(Instant.now(),
				ZoneOffset.ofTotalSeconds(-params.getTimeOffset() * 60).normalized());
		topToday.atTime(23, 59, 59);
		if (creationDate.isAfter(topToday)) {
			throw new AppException("Creation Date must not be after current date");
		}
		
	}

	@Override
	public List<FileMetadataDTO> getFiles() {
		return repository.findAll(Sort.by(Sort.DEFAULT_DIRECTION, "title")).stream().map(x -> mapper.fromEntityToMetadataDTO(x))
						.collect(Collectors.toList());
	}
	
	@Override
	public ServedFileDTO readFile(final String id) throws AppException, IOException {
		final var entity = repository.findById(id).orElseThrow(() -> new AppException("Invalid file"));
		final var data = fileManager.readFileData(id, entity.getExtension());
		return new ServedFileDTO(data, mapper.fromEntityToMetadataDTO(entity));
	}

}
