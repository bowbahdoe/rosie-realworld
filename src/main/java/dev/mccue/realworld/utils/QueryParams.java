package dev.mccue.realworld.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class QueryParams {
    private QueryParams() {}

    public static Map<String, String> parse(String queryString) {
        return Arrays.stream(queryString.split("&"))
                .map(query -> query.split("="))
                .filter(query -> query.length == 2)
                .collect(Collectors.toUnmodifiableMap(
                        query -> URLDecoder.decode(query[0], StandardCharsets.UTF_8),
                        query -> URLDecoder.decode(query[1], StandardCharsets.UTF_8)
                ));
    }
}
