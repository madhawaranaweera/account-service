package com.anz.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse extends RepresentationModel<TransactionResponse> {
    @NotEmpty
    List<@Valid Transaction> transactions;
}
