package com.example.YNN.util;

import jakarta.persistence.AttributeConverter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EmojiConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(
                attribute.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(dbData);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
