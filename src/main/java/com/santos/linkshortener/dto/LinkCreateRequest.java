package com.santos.linkshortener.dto;

import lombok.Getter;

public record LinkCreateRequest(
        String urlOriginal,
        String urlCurta)
{
}
