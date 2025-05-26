package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.TutorAtualizarDto;
import br.com.alura.adopet.api.dto.TutorCadastroDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    public ResponseEntity<String> cadastrar(TutorCadastroDto dto) {
        boolean telefoneJaCadastrado = tutorRepository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = tutorRepository.existsByEmail(dto.email());

        if (telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        } else {
            Tutor tutor = new Tutor(dto);
            tutorRepository.save(tutor);
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<String> atualizar(TutorAtualizarDto dto) {
        Tutor tutor = new Tutor(dto, listarAdocaoPeloTutor(dto));
        tutorRepository.save(tutor);
        return ResponseEntity.ok().build();
    }

    private List<Adocao> listarAdocaoPeloTutor(TutorAtualizarDto dto){
        return tutorRepository.findById(dto.id()).get().getAdocoes();
    }
}
