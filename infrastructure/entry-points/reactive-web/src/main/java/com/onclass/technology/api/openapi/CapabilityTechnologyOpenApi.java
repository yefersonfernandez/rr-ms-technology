package com.onclass.technology.api.openapi;

import com.onclass.technology.api.dto.request.CapabilityTechnologyRequestDto;
import com.onclass.technology.api.dto.response.ApiResponseDto;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class CapabilityTechnologyOpenApi {

    private static final String TAG = "CapabilityTechnology";
    private static final String OK_CODE = String.valueOf(HttpStatus.OK.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String NOT_FOUND_CODE = String.valueOf(HttpStatus.NOT_FOUND.value());
    private static final String INTERNAL_ERROR_CODE = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

    private static final String OK_DESC = "Association successful";
    private static final String BAD_REQUEST_DESC = "Invalid request";
    private static final String NOT_FOUND_DESC = "Not found";
    private static final String INTERNAL_ERROR_DESC = "Internal server error";

    private static final String OPERATION_ASSOCIATE = "associateTechnologies";
    private static final String OPERATION_DESC = "Associates a list of technologies to an existing capability.";

    public void associateTechnologies(Builder builder) {
        builder
                .operationId(OPERATION_ASSOCIATE)
                .description(OPERATION_DESC)
                .tag(TAG)
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(CapabilityTechnologyRequestDto.class))))
                .response(responseBuilder()
                        .responseCode(OK_CODE)
                        .description(OK_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(ApiResponseDto.class))))
                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description(BAD_REQUEST_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(ApiResponseDto.class))))
                .response(responseBuilder()
                        .responseCode(NOT_FOUND_CODE)
                        .description(NOT_FOUND_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(ApiResponseDto.class))))
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(ApiResponseDto.class))));
    }
}
