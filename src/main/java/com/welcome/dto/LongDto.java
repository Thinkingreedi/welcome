package com.welcome.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.welcome.util.LongJsonDeserializer;
import com.welcome.util.LongJsonSerializer;
import lombok.Data;

@Data
public class LongDto {
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;
}
