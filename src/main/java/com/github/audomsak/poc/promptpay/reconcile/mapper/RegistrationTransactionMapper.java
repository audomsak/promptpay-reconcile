package com.github.audomsak.poc.promptpay.reconcile.mapper;

import com.github.audomsak.poc.promptpay.reconcile.model.dto.RegistrationTransactionDto;
import com.github.audomsak.poc.promptpay.reconcile.persistence.RegistrationTransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RegistrationTransactionMapper {
    RegistrationTransactionDto toPojo(RegistrationTransactionEntity entity);
}
