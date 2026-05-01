package com.datamelt.utilities.datagenerator.format;

import com.datamelt.utilities.datagenerator.generate.Row;
import com.datamelt.utilities.datagenerator.generate.RowField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonFormatter
{
    private JsonFormatter() {}
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJsonString(Row row) throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(rowToNestedMap(row));
    }

    public static JsonNode convertToJsonNode(Row row) throws JsonProcessingException
    {
        return objectMapper.convertValue(rowToNestedMap(row), JsonNode.class);
    }


    private static Map<String, Object> rowToNestedMap(Row row)
    {
        Map<String, Object> result = new LinkedHashMap<>();

        for (RowField field : row.getFields())
        {
            String fieldName = field.getName();
            Object value = field.getValue();

            if (!fieldName.contains("."))
            {
                result.put(fieldName, value);
            }
            else
            {
                setNestedValue(result, fieldName, value);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static void setNestedValue(Map<String, Object> root, String dotPath, Object value)
    {
        String[] parts = dotPath.split("\\.", -1);
        Map<String, Object> current = root;

        for (int i = 0; i < parts.length - 1; i++)
        {
            current = (Map<String, Object>) current.computeIfAbsent(
                    parts[i], k -> new LinkedHashMap<String, Object>());
        }

        current.put(parts[parts.length - 1], value);
    }
}
