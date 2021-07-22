package br.com.compasso.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchQueryDto {
    private String query;
    private FilterRequestDto filter;
    private Integer page;
    private Integer size;
}