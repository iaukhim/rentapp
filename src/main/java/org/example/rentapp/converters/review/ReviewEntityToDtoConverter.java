package org.example.rentapp.converters.review;

import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import org.example.rentapp.dtos.ReviewDto;
import org.example.rentapp.entities.Review;
import org.example.rentapp.entities.Review_;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

public class ReviewEntityToDtoConverter implements Converter<Review, ReviewDto> {

    private final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Override
    public ReviewDto convert(MappingContext<Review, ReviewDto> context) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(new Condition<>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof HibernateProxy || context.getSource() instanceof PersistentCollection<?>);
            }
        });
        modelMapper.addMappings(new PropertyMap<Review, ReviewDto>() {
            @Override
            protected void configure() {
                if (!persistenceUtil.isLoaded(context.getSource(), Review_.FACILITY)) {
                    skip(destination.getFacilityDto());
                }
                if (!persistenceUtil.isLoaded(context.getSource(), Review_.ORDER)) {
                    skip(destination.getOrderDto());
                }
            }
        });
        return modelMapper.map(context.getSource(), context.getDestinationType());
    }
}

