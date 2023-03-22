package org.example.rentapp.additional.sorts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.rentapp.entities.Order_;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum OrderSort {
    ID_ASC(Sort.by(Sort.Direction.ASC, Order_.ID)),
    ID_DESC(Sort.by(Sort.Direction.DESC, Order_.ID)),
    PLANNED_DATE_ASC(Sort.by(Sort.Direction.ASC, Order_.PLANNED_DATE)),
    PLANNED_DATE_DESC(Sort.by(Sort.Direction.DESC, Order_.PLANNED_DATE)),
    CREATION_DATE_ASC(Sort.by(Sort.Direction.ASC, Order_.CREATION_DATE)),
    CREATION_DATE_DESC(Sort.by(Sort.Direction.DESC, Order_.CREATION_DATE));

    private final Sort sortValue;
}
