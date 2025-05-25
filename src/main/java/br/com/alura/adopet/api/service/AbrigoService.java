package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository abrigoRepository;

    public List<Abrigo> listar(){
        return abrigoRepository.findAll();
    }

    public void cadastrar(CadastroAbrigoDto dto){
        boolean nomeJaCadastrado = abrigoRepository.existsByNome(dto.nome());
        boolean telefoneJaCadastrado = abrigoRepository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = abrigoRepository.existsByEmail(dto.email());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        } else {
            Abrigo abrigo = new Abrigo(dto);
            abrigoRepository.save(abrigo);
        }
    }

    public List<Pet> listarPets(String idOuNome){
        try {
            Long id = Long.parseLong(idOuNome);
            List<Pet> pets = abrigoRepository.getReferenceById(id).getPets();
            return pets;
        } catch (EntityNotFoundException enfe) {
            throw new ValidacaoException("Abrigo não encontrado!");
        } catch (NumberFormatException e) {
            try {
                List<Pet> pets = abrigoRepository.findByNome(idOuNome).getPets();
                return pets;
            } catch (EntityNotFoundException enfe) {
                throw new ValidacaoException("Abrigo não encontrado!");
            }
        }
    }

    public void cadastrarPet(String idOuNome, CadastroPetDto dto){
        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = abrigoRepository.getReferenceById(id);

            Pet pet = new Pet(dto);
            pet.atribuirAbrigo(abrigo);
            abrigo.getPets().add(pet);
            abrigoRepository.save(abrigo);



        } catch (EntityNotFoundException enfe) {
            throw new ValidacaoException("Abrigo não foi encontrado");
        } catch (NumberFormatException nfe) {
            try {
                Abrigo abrigo = abrigoRepository.findByNome(idOuNome);
                Pet pet = new Pet(dto);
                pet.atribuirAbrigo(abrigo);
                abrigo.getPets().add(pet);
                abrigoRepository.save(abrigo);
            } catch (EntityNotFoundException enfe) {
                throw new ValidacaoException("Abrigo não foi encontrado");
            }
        }
    }
}
