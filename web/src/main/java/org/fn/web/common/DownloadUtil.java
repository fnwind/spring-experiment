package org.fn.web.common;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class DownloadUtil {
    public static ResponseEntity<byte[]> download(String filename, byte[] data) {
        ContentDisposition disposition = buildDisposition(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(data.length)
                .body(data);
    }


    public static ResponseEntity<Resource> download(File file) {
        FileSystemResource resource = new FileSystemResource(file);

        ContentDisposition disposition = buildDisposition(file.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(file.length())
                .body(resource);
    }

    public static ResponseEntity<Resource> download(String filename, InputStream inputStream) {
        InputStreamResource resource = new InputStreamResource(inputStream);

        return download(filename, resource);
    }

    public static ResponseEntity<Resource> download(String filename, Resource resource) {
        ContentDisposition disposition = buildDisposition(filename);
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());

        try {
            long contentLength = resource.contentLength();
            if (contentLength >= 0) {
                builder.contentLength(contentLength);
            }
        } catch (IOException e) {
            // ignore, content length is optional
        }

        return builder.body(resource);
    }

    public static StreamingResponseBody download(HttpServletResponse httpServletResponse,
                                                 String filename, StreamingResponseBody streamingResponseBody) {
        ContentDisposition disposition = buildDisposition(filename);
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return streamingResponseBody;
    }

    private static ContentDisposition buildDisposition(String filename) {
        return ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
    }
}
