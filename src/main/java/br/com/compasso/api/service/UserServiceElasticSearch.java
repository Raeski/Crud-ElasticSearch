package br.com.compasso.api.service;

import br.com.compasso.api.DTO.FilterRequestDto;
import br.com.compasso.api.DTO.SearchQueryDto;
import br.com.compasso.api.model.User;
import br.com.compasso.api.repository.UserRepositoryElasticSearch;
import br.com.compasso.api.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceElasticSearch implements UserRepositoryElasticSearch {

    private static final String INDEX_NAME = "userindex";

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;


    @Override
    public SearchResponse search(SearchQueryDto searchQueryDto) throws IOException{
        SearchRequest searchRequest = Requests.searchRequest(INDEX_NAME);
        if(validateFieldsSearchUser(searchQueryDto) == false)
            throw new IOException("Faltou colocar campo para pesquisa");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
              .should(QueryBuilders.matchQuery("name", searchQueryDto.getQuery()))
              .should(QueryBuilders.matchQuery("id", searchQueryDto.getQuery()));
        if(searchQueryDto.getQuery() != null) {
            FilterRequestDto filter = searchQueryDto.getFilter();
                for (String keyToFilter : filter.getMatch().keySet()) {
                    Object valueToFilter = filter.getMatch().get(keyToFilter).toString().toLowerCase();
                    boolQueryBuilder.filter(QueryBuilders.termQuery(keyToFilter, valueToFilter)); }
        }
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .from(0)
                .size(5)
                .query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }


    public IndexResponse save(User user) throws IOException {

        IndexRequest request = Requests.indexRequest(INDEX_NAME)
                .id(user.getId())
                .source(convertUserToMap(user));
        RequestOptions options = RequestOptions.DEFAULT;
        return restHighLevelClient.index(request, options);
    }

    private Map<String, Object> convertUserToMap(User user) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(user);
        return objectMapper.readValue(json, Map.class);
    }

    public BulkResponse saveAll(List<User> users) throws IOException {
        BulkRequest request = Requests.bulkRequest();

        users.forEach(user -> {
            try {
                IndexRequest indexRequest = Requests
                        .indexRequest(INDEX_NAME)
                        .source(convertUserToMap(user));
                request.add(indexRequest);

            }catch ( JsonProcessingException e) {
                e.getMessage();
            }
        });
        RequestOptions options = RequestOptions.DEFAULT;
        return restHighLevelClient.bulk(request, options);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<User> update( User user ) {
        Optional<User> user1 = userRepository.findById(user.getId());
        if(!user1.isEmpty()){
            if(validateFieldsUpdateUser(user1, user)) {
                return new ResponseEntity("O campo CPF não pode ser alterado!", HttpStatus.BAD_REQUEST);
            }
            user1.get().setId(user.getId());
            user1.get().setName(user.getName());
            user1.get().setCpf(user.getCpf());
            userRepository.save(user1.get());
            return new ResponseEntity<>( user, HttpStatus.OK );
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND );
    }

    private boolean validateFieldsSearchUser(SearchQueryDto searchQueryDto) {
        if(searchQueryDto.getFilter().getMatch().containsValue("") || searchQueryDto.getFilter().getMatch().isEmpty() ) {
            return false;
        } else{
            return true;
        }
    }

    private boolean validateFieldsUpdateUser(Optional<User> newUser, User user) {

        if ( !newUser.get().getCpf().equalsIgnoreCase(user.getCpf()) ) {
            return true;
        }
        return false;
    }

}
