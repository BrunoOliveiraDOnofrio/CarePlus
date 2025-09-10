package com.example.careplus.controller;

import com.example.careplus.exception.UserNotExistsException;
import com.example.careplus.model.Usuario;
import com.example.careplus.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")
public class UsuarioController {
//    List<Usuario> usuarios = new ArrayList<>();

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = service.listarTodos();

        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> listarPorId(@PathVariable Integer id){

            Usuario usuario = service.listarPorId(id);
            return ResponseEntity.ok().body(usuario);

    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario){
//        usuarios.add(usuario);
        try {
            Usuario usuarioSalvo = service.salvar(usuario);
            return ResponseEntity.status(201).body(usuarioSalvo);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarUsuario(@PathVariable Integer id){
        try{
            service.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario, @PathVariable Integer id){
        try{
            service.atualizar(usuario, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/por-email")
    public ResponseEntity<List<Usuario>> listarPorEmail(@RequestParam String email){
        List<Usuario> usuario = service.listarPorEmail(email);
        return ResponseEntity.ok().body(usuario);

    }


}

