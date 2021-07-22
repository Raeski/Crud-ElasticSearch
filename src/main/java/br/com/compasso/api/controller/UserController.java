package br.com.compasso.api.controller;


import br.com.compasso.api.DTO.SearchQueryDto;
import br.com.compasso.api.model.User;
import br.com.compasso.api.service.UserServiceElasticSearch;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    public UserServiceElasticSearch userService;



    @PostMapping("/bulk")
    public BulkResponse addUsers(@RequestBody List<User> userList) throws IOException{
        return userService.saveAll(userList);
    }

    @PostMapping("/search")
    public SearchResponse searchUser(@RequestBody SearchQueryDto searchQueryDto) throws IOException {
        return (SearchResponse) userService.search(searchQueryDto);
    }

    @PostMapping
    public IndexResponse addUser(@RequestBody User user) throws IOException {
        return userService.save(user);
    }

    @DeleteMapping("/delete")
    public void delete (@RequestBody User user) {
         userService.delete(user);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update (@RequestBody User user) {
        return new ResponseEntity(userService.update(user), HttpStatus.OK);
    }

}
