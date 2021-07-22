package br.com.compasso.api.repository;

import br.com.compasso.api.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, String> {
    void deleteById(Long id);
}
