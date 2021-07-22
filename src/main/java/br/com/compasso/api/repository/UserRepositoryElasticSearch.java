package br.com.compasso.api.repository;

import br.com.compasso.api.DTO.SearchQueryDto;
import br.com.compasso.api.model.User;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface UserRepositoryElasticSearch {

    SearchResponse search(SearchQueryDto searchQueryDto) throws IOException;

    IndexResponse save(User product) throws IOException;

    BulkResponse saveAll(List<User> products) throws IOException;


}
