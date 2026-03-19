package com.vnn.urlshortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenRequest {

    @NotBlank(message = "URL không được để trống")
    @Pattern(
            regexp = "^$|^https?://.*",
            message = "URL phải bắt đầu bằng http:// hoặc https://"
    )
    private String url;
}
