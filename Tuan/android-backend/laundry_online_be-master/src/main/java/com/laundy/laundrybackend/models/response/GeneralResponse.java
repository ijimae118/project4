package com.laundy.laundrybackend.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {
    @JsonProperty("statusCd")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;
}
