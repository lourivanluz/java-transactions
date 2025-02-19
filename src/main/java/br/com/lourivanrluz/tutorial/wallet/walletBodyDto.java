package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record walletBodyDto(
    @Schema(example = "500")
    BigDecimal  value
) {}
