package dev.ovidio.exception;

import dev.ovidio.record.ExceptionResponseRecord;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BaseExceptionMapper implements ExceptionMapper<BaseException> {
    @Override
    public Response toResponse(BaseException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionResponseRecord(exception))
                .build();
    }
}
