package br.com.victorvale.academymanagerbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private Integer age;
    private String belt;
    private BigDecimal weight;
    private BigDecimal height;

    @JsonProperty("active")
    private boolean isActive;
}
