package dotsub.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import dotsub.demo.model.DotsubFile;

public interface DotsubFileRepo extends JpaRepository<DotsubFile, String>{


}
