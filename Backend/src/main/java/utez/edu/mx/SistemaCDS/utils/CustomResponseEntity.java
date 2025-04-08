package utez.edu.mx.SistemaCDS.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// PLANTILLA PARA RESPUESTAS HTTP
@Service
public class CustomResponseEntity {
    Map<String, Object> body;

    public ResponseEntity<?> getOkResponse(String message, Object data ){
        body = new HashMap<>();

        body.put("message", message);
        body.put("status", "OK");
        body.put("code", "200");
        if(data != null) {
            body.put("data", data);
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<?> getOkLoginResponse(String message, long id,  String rol, Object token ){
        body = new HashMap<>();

        body.put("message", message);
        body.put("status", "OK");
        body.put("code", "200");
        body.put("id", id);
        body.put("rol", rol);
        if(token != null) {
            body.put("token", token);
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<?> get201Response(String message, Object data ){
        body = new HashMap<>();

        body.put("message", message);
        body.put("status", "CREATED");
        body.put("code", "201");
        if(data != null) {
            body.put("data", data);
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<?> get400Response(String message) {
        body = new HashMap<>();

        body.put("message", message);
        body.put("status", "BAD_REQUEST");
        body.put("code", "400");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> get404Response(String message){
        body = new HashMap<>();

        body.put("message", message);
        body.put("status", "NOT_FOUND");
        body.put("code", "404");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
