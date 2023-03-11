package org.example.rentapp.dtos.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.rentapp.dtos.CityDto;

import java.io.IOException;

public class CustomCitySerializer extends StdSerializer<CityDto> {
    public CustomCitySerializer() {
        super(CityDto.class);
    }

    public CustomCitySerializer(JavaType type) {
        super(type);
    }

    public CustomCitySerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    public CustomCitySerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(CityDto cityDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

    }
}
