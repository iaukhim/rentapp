package org.example.rentapp.additional.sorts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.rentapp.entities.Facility_;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum FacilitySort {

    ID_ASC(Sort.by(Sort.Direction.ASC, Facility_.ID)),
    ID_DESC(Sort.by(Sort.Direction.DESC, Facility_.ID)),
    NAME_ASC(Sort.by(Sort.Direction.ASC, Facility_.NAME)),
    NAME_DESC(Sort.by(Sort.Direction.DESC, Facility_.NAME)),
    SPACE_ASC(Sort.by(Sort.Direction.ASC, Facility_.SPACE)),
    SPACE_DESC(Sort.by(Sort.Direction.DESC, Facility_.SPACE));

    private final Sort sortValue;

}
