package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.TutorAtualizarDto;
import br.com.alura.adopet.api.dto.TutorCadastroDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid TutorCadastroDto dto) {

        try{
            tutorService.cadastrar(dto);
            return ResponseEntity.ok().build();
        }catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid TutorAtualizarDto dto) {
        try{
            tutorService.atualizar(dto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
