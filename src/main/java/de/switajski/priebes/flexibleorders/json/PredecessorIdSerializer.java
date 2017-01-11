package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Transactional
@Component
public final class PredecessorIdSerializer
        extends JsonSerializer<ReportItem> {

    @Override
    public void serialize(ReportItem value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("documentNo", value.getReport().getDocumentNumber());
        gen.writeStringField("position", value.getPosition().toString());
        gen.writeEndObject();
    }

}
