/*
package org.example.rentapp.dtos.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.dtos.CountryDto;

import java.io.IOException;
import java.util.Objects;

public class CustomCityDeserializer extends StdDeserializer<CityDto> {
    public CustomCityDeserializer() {
        super(CityDto.class);
    }

    public CustomCityDeserializer(JavaType valueType) {
        super(valueType);
    }

    public CustomCityDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public CityDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        CityDto cityDto = new CityDto();

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonNode idNode = node.get("id");
        if (!Objects.isNull(idNode)) {
            cityDto.setId(idNode.asLong(-1));
        }

        JsonNode nameNode = node.get("name");
        if (!Objects.isNull(nameNode)) {
            cityDto.setName(nameNode.asText(""));
        }

        CountryDto countryDto = new CountryDto();
        JsonNode countryNode = node.get("country");
        if (!Objects.isNull(countryNode)) {
            countryDto.setId(countryNode.get("id").asLong(-1));
            countryDto.setCode(countryNode.get("code").asText(""));
            countryDto.setName(countryNode.get("name").asText(""));
        }

        cityDto.setCountryCode(countryDto.getCode());
        cityDto.setCountryId(countryDto.getId());
        cityDto.setCountryName(countryDto.getName());

        return cityDto;
    }
}
*/
