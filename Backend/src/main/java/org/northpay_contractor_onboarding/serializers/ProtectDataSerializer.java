package org.northpay_contractor_onboarding.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;


public class ProtectDataSerializer extends JsonSerializer<String> { // <-- CAMBIADO A String

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Al heredar de JsonSerializer<String>, 'value' ya es un String directo
        if (value == null) {
            gen.writeNull();
            return;
        }

        // Salvaguarda total para valores por defecto
        if (value.equalsIgnoreCase("N/A") || value.equalsIgnoreCase("Pending") || value.isBlank()) {
            gen.writeString(value);
            return;
        }

        String maskedValue;

        // Clasificación inteligente por contenido
        if (value.contains("@")) {
            // Caso: Email
            maskedValue = value.replaceAll("(?<=.{3}).(?=.*@)", "*");
        } else if (value.startsWith("+") || value.matches("^[0-9\\s\\-]+$") && value.length() > 6) {
            // Caso: Teléfono
            maskedValue = value.replaceAll("\\w(?=\\w{4})", "x");
        } else {
            // Caso: CBU, DNI o Wallet Crypto
            maskedValue = value.length() > 4 
                    ? "xx.xxx." + value.substring(value.length() - 3)
                    : "****";
        }

        gen.writeString(maskedValue);
    }
}

