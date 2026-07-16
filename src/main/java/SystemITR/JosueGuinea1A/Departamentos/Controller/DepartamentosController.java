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
@CrossOrigin
// en Spring Framework sirve para autorizar peticiones web (como llamadas Fetch o AJAX)
// que provienen de un dominio diferente al de tu servidor backend. Por seguridad, los navegadores bloquean estas peticiones de origen cruzado;
// esta anotación permite definir excepciones de manera controlada.




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
           //Esto sirve para Eliminar
           // Indica que este método manejará peticiones HTTP DELETE que lleguen a la ruta "/{id}" (ej. /departamentos/5)
           @DeleteMapping("/{id}")
// Define un método público que devuelve un contenedor de respuesta HTTP (ResponseEntity) que adentro lleva tu clase personalizada 'ApiResponse'
           public ResponseEntity<ApiResponse<DepartamentosDTO>> eliminarDepartamento(
                   // Captura el parámetro "{id}" de la URL y lo convierte en una variable de tipo Long llamada 'id'
                   @PathVariable Long id) {

               // Inicia un bloque try-catch para capturar y manejar cualquier error inesperado que ocurra durante el proceso
               try {

                   // Llama al servicio para eliminar el registro y guarda el resultado (true si se eliminó, false si no existía)
                   boolean respuesta = service.eliminarInfo(id);

                   // Evalúa si la eliminación fue exitosa (si 'respuesta' es true)
                   if (respuesta) {

                       // Instancia un nuevo objeto de respuesta exitosa. Parámetros: Éxito = true, Mensaje informativo, Datos = null (porque ya se eliminó)
                       ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(
                               true,
                               "Dato con ID " + id + " eliminado exitosamente", // Ojo: añadí un espacio aquí para que no se pegue el texto
                               null);

                       // Retorna un estado HTTP 204 (NO_CONTENT) junto con el cuerpo de la respuesta exitosa en formato JSON
                       return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosa);

                   } // Fin del bloque 'if'

                   // Las siguientes líneas se ejecutarán solo si la eliminación no se pudo completar (cuando 'respuesta' es false)

                   // Instancia un objeto de respuesta indicando que la operación falló. Parámetros: Éxito = false, Mensaje, Datos = null
                   ApiResponse<DepartamentosDTO> respuestaNoRealizado = new ApiResponse<>(
                           false,
                           "El proceso de eliminación no se pudo completar",
                           null);

                   // Registra un mensaje informativo en la consola del servidor advirtiendo que el ID buscado no existía
                   log.info("Departamento con ID: " + id + ", no fue encontrado");

                   // Retorna un estado HTTP 404 (NOT_FOUND) junto con el cuerpo de la respuesta que explica que no se completó
                   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoRealizado);
// Fin del bloque 'try'
               }

               // Atrapa cualquier excepción/error que haya ocurrido dentro del bloque 'try' para evitar que la aplicación se caiga por completo
               catch (Exception e) {

                   // Registra un mensaje de error crítico en la consola del servidor para que el programador pueda revisarlo
                   log.error("Error crítico, consulte con el administrador");

                   // Imprime en la consola toda la pila del error (la ruta exacta de los archivos y líneas donde falló el código)
                   e.printStackTrace();

                   // Instancia un objeto de respuesta amigable para el cliente. Parámetros: Éxito = false, Mensaje de error genérico, Datos = null
                   ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(
                           false,
                           "Error inesperado, consulte con el administrador para solucionar el problema",
                           null);

                   // Retorna un estado HTTP 500 (INTERNAL_SERVER_ERROR) con el JSON de error para no exponer detalles técnicos sensibles al cliente
                   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);

               } // Fin del bloque 'catch'

           } // Fin del método eliminarDepartamento

    @PutMapping("/{id}")
    //@Valid sirve para que las validaciones que estan escritas en el DTO puedan ejecutarce
    public ResponseEntity<ApiResponse<DepartamentosDTO>> actualizarData(@PathVariable Long id, @Valid @RequestBody DepartamentosDTO dto){
        try{
           DepartamentosDTO data = service.actualizar(id, dto);
           if(data != null) {
               log.info("Departamento con ID: " + id + " ha sido actualizado. ");
               ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true, "Departamento con ID:" + id + " ha sido actualizadp. ", data);
               return ResponseEntity.ok(respuestaExitosa);
           }
           log.warn("No se pudo completar la actualizacion del departamento con ID: " + id);
           ApiResponse<DepartamentosDTO> repuestaNoCompletada = new ApiResponse<>( false, "No se pudo completar la actualizacion del departamento con ID: " + id);
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(repuestaNoCompletada);
        }catch (Exception e){
            log.info("Error Critico a Actualizar el departamento con ID: " + id);
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false,"Error critico al actualizar el departamento con ID: ", null);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @GetMapping("/abreviatura/{abreviatura}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> buscarPorAbreviatura(@PathVariable String abreviatura){
        try{
            DepartamentosDTO data = service.buscarDepartamentoAbreviatura(abreviatura);
            if(data != null){
                log.info("Se obtuvieron los datos del departamento con abreviatura " + abreviatura);
                ApiResponse<DepartamentosDTO> respuestaExito = new ApiResponse<>(true, "\"Se obtuvieron los datos del departamento con abreviatura " + abreviatura , data);
            return ResponseEntity.ok(respuestaExito);
            }
            log.warn("Departamento no encontrado " + abreviatura);
            ApiResponse<DepartamentosDTO> respuestaNoEncontrada = new ApiResponse<>(
                    false,"Departamento no encontrado: " + abreviatura);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }catch (Exception e){
            log.info("No hay departamentos regstitrado con ese Abreviatura");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false,"Nose pudo completar la busqueda del ID: ", null);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


}
