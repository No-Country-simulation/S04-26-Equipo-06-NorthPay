package org.northpay_contractor_onboarding.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ProtectDataSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        String input = value.toString();
        String fieldName = gen.getOutputContext().getCurrentName();
        String maskedValue;

        if (fieldName != null && fieldName.toLowerCase().contains("email")) {
         
            maskedValue = input.replaceAll("(?<=.{3}).(?=.*@)", "*");

        } else if (fieldName != null
                && (fieldName.toLowerCase().contains("phone") || fieldName.toLowerCase().contains("account"))) {
        
            maskedValue = input.replaceAll("\\w(?=\\w{4})", "x");

        } else {
            
            maskedValue = input.length() > 4
                    ? "xx.xxx." + input.substring(input.length() - 3)
                    : "****";
        }

        gen.writeString(maskedValue);
    }

}
