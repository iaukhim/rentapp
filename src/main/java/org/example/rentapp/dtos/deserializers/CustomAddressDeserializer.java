/*
package org.example.rentapp.dtos.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.dtos.CityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAddressDeserializer extends StdDeserializer<AddressDto> {
    @Autowired
    private CustomCityDeserializer customCityDeserializer;

    public CustomAddressDeserializer(CustomCityDeserializer customCityDeserializer) {
        super(AddressDto.class);
        this.customCityDeserializer = customCityDeserializer;
    }

    @Override
    public AddressDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        JsonParser jp = codec.getFactory().createParser(node.get("city").toString());
        jp.setCodec(codec);
        CityDto cityDto = customCityDeserializer.deserialize(jp, ctxt);

        AddressDto addressDto = new AddressDto();
        addressDto.setCityId(cityDto.getId());
        addressDto.setCityName(cityDto.getName());
        addressDto.setCountryId(cityDto.getCountryId());
        addressDto.setCountryCode(cityDto.getCountryCode());
        addressDto.setCountryName(cityDto.getCountryName());

        JsonNode idNode = node.get("id");
        if (!Objects.isNull(idNode)) {
            addressDto.setId(idNode.asLong());
        }

        JsonNode districtNode = node.get("district");
        if (!Objects.isNull(districtNode)) {
            addressDto.setDistrict(districtNode.asText(""));
        }

        JsonNode streetNameNode = node.get("streetName");
        if (!Objects.isNull(streetNameNode)) {
            addressDto.setStreetName(streetNameNode.asText(""));
        }

        JsonNode streetNumberNode = node.get("streetNumber");
        if (!Objects.isNull(streetNumberNode)) {
            addressDto.setStreetNumber(streetNumberNode.asText(""));
        }

        return addressDto;
    }
}
*/
