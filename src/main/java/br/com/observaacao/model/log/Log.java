package br.com.observaacao.model.log;

import java.time.LocalDateTime;

public class Log {
    private Long id;
    private Long idUsuario;
    private String nomeTabela;
    private String acao;
    private String dadosAlterados; // Armazena o JSON
    private LocalDateTime dataExecucao;

    public Log() {}

    public Log(Long idUsuario, String nomeTabela, String acao, String dadosAlterados) {
        this.idUsuario = idUsuario;
        this.nomeTabela = nomeTabela;
        this.acao = acao;
        this.dadosAlterados = dadosAlterados;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNomeTabela() { return nomeTabela; }
    public void setNomeTabela(String nomeTabela) { this.nomeTabela = nomeTabela; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getDadosAlterados() { return dadosAlterados; }
    public void setDadosAlterados(String dadosAlterados) { this.dadosAlterados = dadosAlterados; }

    public LocalDateTime getDataExecucao() { return dataExecucao; }
    public void setDataExecucao(LocalDateTime dataExecucao) { this.dataExecucao = dataExecucao; }
}
