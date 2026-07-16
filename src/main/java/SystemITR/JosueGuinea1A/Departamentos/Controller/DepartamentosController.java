package SystemITR.JosueGuinea1A.Departamentos.Controller;

import SystemITR.JosueGuinea1A.Departamentos.DTO.DepartamentosDTO;
import SystemITR.JosueGuinea1A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/departamento")
public class DepartamentosController {


    private final DepartamentosService service;
    public DepartamentosController(DepartamentosService service) {
        this.service = service;
    }

    @PostMapping
    //Requestbody sirve para Ingresar Datos
    public ResponseEntity<ApiResponse<DepartamentosDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentosDTO json){
        try{
            //Creamos un objeto DTO porque el service.insertarDatos retornará un objeto de tipo DepartamentosDTO
            DepartamentosDTO objDTO = service.insertarDatos(json);
            if (objDTO == null){
                ApiResponse respuesta = new ApiResponse(false,"No se pudo completar el proceso de inserción", json);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            ApiResponse respuesta = new ApiResponse(true, "Dato ingresado exitosamente", objDTO);
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuesta = new ApiResponse<>(false, "Error critico"+ e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity <ApiResponse<List<DepartamentosDTO>>> obtenerDepartamentos(){
        try{
            List<DepartamentosDTO> listaDTO = service.listarTodos();
            if (listaDTO != null){
                ApiResponse<List<DepartamentosDTO>> respuestaExitosa = new ApiResponse<>(true,"Proceso completado", listaDTO);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<List<DepartamentosDTO>> respuestaNoData = new ApiResponse<>(true,"No hay datos por mostrar" , null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoData);
        }catch (Exception e){
            log.info("No hay departamentos registrados");
            e.printStackTrace();
            ApiResponse <List<DepartamentosDTO>> respuestaError = new ApiResponse<>(false,"Nose pudo completar la busqueda del ID: ",null);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    //Todo eso es para buscar un valor en concreto
    @GetMapping("/{id}")
    //El retono sirve para que te devuelva un objeto en especifico
    public ResponseEntity <ApiResponse<DepartamentosDTO>> obtenerDepartamentosPorId(@PathVariable Long id){
        try{
            DepartamentosDTO dto = service.buscarDepartamento(id);
            if (dto != null ){
                log.info("Se obtuvieron los datos del departamento: " + dto);
               //Armar la respuesta utilizando ApiResponse
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>( true,"Dato encontrado" + id , dto);
                        return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<DepartamentosDTO> noEncontrado = new ApiResponse<>(false,"DATOS NO ENCONTRADO", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);

        } catch (Exception e){
            log.info("No hay departamentos registrados");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false,"Nose pudo completar la busqueda del ID: ", null);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> eliminarDepartamento(@PathVariable Long id) {
        try {
            boolean respuesta = service.eliminarInfo(id);
            if (respuesta) {
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(
                        true,
                        "Dato con ID" + id + "Eliminado exitosamente",
                        null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosa);

            }
            //Las siguientes lineas se ejecutaran solo si la eliminacion no se pudo completar
            ApiResponse<DepartamentosDTO> respuestaNoRealizado = new ApiResponse<>(
                    false,
                    "El proceso de eliminacion no se pudo completar",
                    null);
            log.info("Departamento con ID: " + id + ", no fue encontrado");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoRealizado);

        }

        catch (Exception e) {
            //Log es un mensaje que queda registrado en el historial del servidor
            log.error("Error critico , consulte con el admnistrador");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false, "Error inesperado , consulte con el administrador para solucionar el problema", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
