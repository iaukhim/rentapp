package org.example.rentapp.converters.facility;

import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import lombok.RequiredArgsConstructor;
import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Facility_;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

@RequiredArgsConstructor
public class FacilityEntityToDtoConverter implements Converter<Facility, FacilityDto> {

    private final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Override
    public FacilityDto convert(MappingContext<Facility, FacilityDto> context) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(new Condition<>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof HibernateProxy || context.getSource() instanceof PersistentCollection<?>);
            }
        });
        modelMapper.addMappings(new PropertyMap<Facility, FacilityDto>() {
            @Override
            protected void configure() {
                if (!persistenceUtil.isLoaded(context.getSource(), Facility_.OWNER)) {
                    skip(destination.getOwner());
                }
            }
        });
        TypeMap<Facility, FacilityDto> typeMap = modelMapper.typeMap(Facility.class, FacilityDto.class);
        typeMap.addMappings(
                mapper -> {
                    if (persistenceUtil.isLoaded(context.getSource(), Facility_.ADDRESS)) {
                        mapper.map(source -> source.getAddress().getId(), (destination, value) -> destination.getAddressDto().setId((Long) value));
                        mapper.map(source -> source.getAddress().getDistrict(), (destination, value) -> destination.getAddressDto().setDistrict((String) value));
                        mapper.map(source -> source.getAddress().getStreetName(), (destination, value) -> destination.getAddressDto().setStreetName((String) value));
                        mapper.map(source -> source.getAddress().getStreetNumber(), (destination, value) -> destination.getAddressDto().setStreetNumber((String) value));
                        mapper.map(source -> source.getAddress().getCity().getId(), (destination, value) -> destination.getAddressDto().getCityDto().setId((Long) value));
                        mapper.map(source -> source.getAddress().getCity().getName(), (destination, value) -> destination.getAddressDto().getCityDto().setName((String) value));
                        mapper.map(source -> source.getAddress().getCity().getCountry().getId(), (destination, value) -> destination.getAddressDto().getCityDto().getCountryDto().setId((Long) value));
                        mapper.map(source -> source.getAddress().getCity().getCountry().getName(), (destination, value) -> destination.getAddressDto().getCityDto().getCountryDto().setName((String) value));
                        mapper.map(source -> source.getAddress().getCity().getCountry().getCode(), (destination, value) -> destination.getAddressDto().getCityDto().getCountryDto().setCode((String) value));
                    }
                });

        FacilityDto facilityDto = modelMapper.map(context.getSource(), FacilityDto.class);
        return facilityDto;
    }
}
