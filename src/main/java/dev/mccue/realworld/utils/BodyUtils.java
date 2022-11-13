package dev.mccue.realworld.utils;

import dev.mccue.json.Json;
import dev.mccue.json.JsonReadException;
import dev.mccue.json.decode.alpha.Decoder;
import dev.mccue.json.decode.alpha.JsonDecodingException;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;

public final class BodyUtils {
    private BodyUtils() {}

    public static <T> T parseBody(Request request, Decoder<T> decoder) {
        try {
            return decoder.decode(Json.read(new InputStreamReader(request.body())));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (JsonReadException | JsonDecodingException e) {
            throw new ValidationException(e);
        }
    }

    private static final class ValidationException extends RuntimeException implements IntoResponse {
        private final IntoResponse intoResponse;
        ValidationException(Throwable e) {
            super(e);
            this.intoResponse = () -> Responses.validationError(List.of(e.getMessage()));
        }

        @Override
        public Response intoResponse() {
            return this.intoResponse.intoResponse();
        }
    }
}
