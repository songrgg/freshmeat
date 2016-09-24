package me.songrgg.freshmeat.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author songrgg
 * @since 1.0
 */
@NoRepositoryBean
public interface ApiCrudRepository<T, IDT extends Serializable>
        extends CrudRepository<T, IDT>, JpaSpecificationExecutor<T> {
  List<T> findByIdGreaterThanOrderByIdAsc(IDT id, Pageable pageable);

  List<T> findByIdLessThanOrderByIdDesc(IDT id, Pageable pageable);

  List<T> findByOrderByIdDesc(Pageable pageable);
}
