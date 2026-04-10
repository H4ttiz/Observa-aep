package br.com.observaacao.service.endereco;

import br.com.observaacao.dao.endereco.DaoEndereco;
import br.com.observaacao.model.endereco.Endereco;
import br.com.observaacao.service.log.ServiceLog;

import java.util.List;

public class ServiceEndereco {

    private final DaoEndereco daoEndereco;
    private final ServiceLog logService;

    public ServiceEndereco(DaoEndereco daoEndereco, ServiceLog logService) {
        this.daoEndereco = daoEndereco;
        this.logService = logService;
    }

    public Endereco cadastrar(Endereco endereco, Long idUsuarioExecutor) {

        validarEndereco(endereco);

        daoEndereco.salvar(endereco);

        String detalhes = String.format(
                "{\"logradouro\": \"%s\", \"numero\": \"%s\", \"cep\": \"%s\"}",
                endereco.getLogradouro(), endereco.getNumero(), endereco.getCep()
        );
        logService.registrarLog(idUsuarioExecutor, "enderecos", "INSERT", detalhes);

        return endereco;
    }

    public Endereco buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Endereco endereco = daoEndereco.buscarPorId(id);
        if (endereco == null) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }

        return endereco;
    }

    public List<Endereco> listarTodos() {
        return daoEndereco.listarTodos();
    }

    public Endereco atualizar(Long id, Endereco endereco, Long idUsuarioExecutor) {
        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Endereco existente = daoEndereco.buscarPorId(id);
        if (existente == null) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }

        validarEndereco(endereco);
        endereco.setId(id);

        daoEndereco.atualizar(endereco);

        String detalhes = String.format(
                "{\"id\": %d, \"novo_cep\": \"%s\", \"novo_num\": \"%s\"}",
                id, endereco.getCep(), endereco.getNumero()
        );
        logService.registrarLog(idUsuarioExecutor, "enderecos", "UPDATE", detalhes);

        return endereco;
    }

    public void desativar(Long id, Long idUsuarioExecutor) {
        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Endereco existente = daoEndereco.buscarPorId(id);
        if (existente == null) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }

        daoEndereco.desativar(id);

        String detalhes = "{\"id_endereco_desativado\": " + id + "}";
        logService.registrarLog(idUsuarioExecutor, "enderecos", "DISABLE", detalhes);
    }

    private void validarEndereco(Endereco endereco) {
        if (endereco.getCep() == null || endereco.getCep().isBlank()) throw new RuntimeException("CEP não pode ser vazio");
        if (endereco.getLogradouro() == null || endereco.getLogradouro().isBlank()) throw new RuntimeException("Logradouro não pode ser vazio");
        if (endereco.getNumero() == null || endereco.getNumero().isBlank()) throw new RuntimeException("Número não pode ser vazio");
        if (endereco.getBairro() == null || endereco.getBairro().isBlank()) throw new RuntimeException("Bairro não pode ser vazio");
        if (endereco.getCidade() == null || endereco.getCidade().isBlank()) throw new RuntimeException("Cidade não pode ser vazia");
        if (endereco.getEstado() == null || endereco.getEstado().isBlank()) throw new RuntimeException("Estado não pode ser vazio");
    }
}