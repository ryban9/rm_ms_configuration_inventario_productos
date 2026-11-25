package ec.rmdetalles.inventario.api;

import ec.rmdetalles.inventario.service.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class) ProblemDetail notFound(ResourceNotFoundException e){return problem(HttpStatus.NOT_FOUND,"No encontrado",e.getMessage());}
    @ExceptionHandler(IllegalArgumentException.class) ProblemDetail badRequest(IllegalArgumentException e){return problem(HttpStatus.BAD_REQUEST,"Solicitud no válida",e.getMessage());}
    @ExceptionHandler(MethodArgumentNotValidException.class) ProblemDetail validation(MethodArgumentNotValidException e){String d=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining("; "));return problem(HttpStatus.BAD_REQUEST,"Error de validación",d);}
    @ExceptionHandler(ResponseStatusException.class) ProblemDetail status(ResponseStatusException e){return problem(HttpStatus.valueOf(e.getStatusCode().value()),"Movimiento no permitido",e.getReason()==null?"El movimiento no puede aplicarse.":e.getReason());}
    @ExceptionHandler(DataIntegrityViolationException.class) ProblemDetail conflict(DataIntegrityViolationException e){return problem(HttpStatus.CONFLICT,"Conflicto de datos","El movimiento infringe una restricción de integridad.");}
    private ProblemDetail problem(HttpStatus s,String title,String detail){ProblemDetail p=ProblemDetail.forStatusAndDetail(s,detail);p.setTitle(title);p.setType(URI.create("about:blank"));return p;}
}
