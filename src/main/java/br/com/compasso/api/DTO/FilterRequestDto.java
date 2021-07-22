package br.com.compasso.api.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FilterRequestDto{
    private Map<String, Object> match;
}
