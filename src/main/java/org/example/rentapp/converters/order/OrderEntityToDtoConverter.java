package org.example.rentapp.converters.order;

import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import org.example.rentapp.dtos.OrderDto;
import org.example.rentapp.entities.Order;
import org.example.rentapp.entities.Order_;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

public class OrderEntityToDtoConverter implements Converter<Order, OrderDto> {

    private final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Override
    public OrderDto convert(MappingContext<Order, OrderDto> context) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(new Condition<>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof HibernateProxy || context.getSource() instanceof PersistentCollection<?>);
            }
        });
        modelMapper.addMappings(new PropertyMap<Order, OrderDto>() {
            @Override
            protected void configure() {
                if (!persistenceUtil.isLoaded(context.getSource(), Order_.FACILITY)) {
                    skip(destination.getFacility());
                }
                if (!persistenceUtil.isLoaded(context.getSource(), Order_.RENTER)) {
                    skip(destination.getRenter());
                }
            }
        });
        return modelMapper.map(context.getSource(), context.getDestinationType());
    }
}
