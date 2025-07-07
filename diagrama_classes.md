```mermaid
classDiagram
    %% Enums
    class TipoUsuario {
        <<enumeration>>
        ADMIN
        PARTICIPANTE
    }
    
    class PerfilParticipante {
        <<enumeration>>
        ALUNO
        PROFESSOR
        PROFISSIONAL
    }
    
    class TipoAtividade {
        <<enumeration>>
        PALESTRA
        SIMPOSIO
        CURSO
        WORKSHOP
    }
    
    class StatusPagamento {
        <<enumeration>>
        PENDENTE
        CONFIRMADO
        CANCELADO
    }
    
    %% Classes de Modelo
    class Usuario {
        -int id
        -String nome
        -String email
        -String senha
        -TipoUsuario tipo
        -PerfilParticipante perfil
        +getId() int
        +setId(int)
        +getNome() String
        +setNome(String)
        +getEmail() String
        +setEmail(String)
        +getSenha() String
        +setSenha(String)
        +getTipo() TipoUsuario
        +setTipo(TipoUsuario)
        +getPerfil() PerfilParticipante
        +setPerfil(PerfilParticipante)
        +equals(Object) boolean
        +hashCode() int
    }
    
    class Administrador {
        +Administrador(String, String, String)
    }
    
    class Participante {
        +Participante(String, String, String, PerfilParticipante)
    }
    
    class Evento {
        -int id
        -String titulo
        -String descricao
        -LocalDateTime dataInicio
        -LocalDateTime dataFim
        -String localizacao
        -int administradorId
        +getId() int
        +setId(int)
        +getTitulo() String
        +setTitulo(String)
        +getDescricao() String
        +setDescricao(String)
        +getDataInicio() LocalDateTime
        +setDataInicio(LocalDateTime)
        +getDataFim() LocalDateTime
        +setDataFim(LocalDateTime)
        +getLocalizacao() String
        +setLocalizacao(String)
        +getAdministradorId() int
        +setAdministradorId(int)
    }
    
    class Atividade {
        -int id
        -String nome
        -String descricao
        -LocalDateTime dataRealizacao
        -int limiteInscritos
        -TipoAtividade tipo
        -int eventoId
        +getId() int
        +setId(int)
        +getNome() String
        +setNome(String)
        +getDescricao() String
        +setDescricao(String)
        +getDataRealizacao() LocalDateTime
        +setDataRealizacao(LocalDateTime)
        +getLimiteInscritos() int
        +setLimiteInscritos(int)
        +getTipo() TipoAtividade
        +setTipo(TipoAtividade)
        +getEventoId() int
        +setEventoId(int)
    }
    
    class Inscricao {
        -int id
        -int participanteId
        -int eventoId
        -int atividadeId
        -LocalDateTime dataInscricao
        -StatusPagamento statusPagamento
        -double valorPago
        +getId() int
        +setId(int)
        +getParticipanteId() int
        +setParticipanteId(int)
        +getEventoId() int
        +setEventoId(int)
        +getAtividadeId() int
        +setAtividadeId(int)
        +getDataInscricao() LocalDateTime
        +setDataInscricao(LocalDateTime)
        +getStatusPagamento() StatusPagamento
        +setStatusPagamento(StatusPagamento)
        +getValorPago() double
        +setValorPago(double)
    }
    
    class ValorInscricao {
        -int eventoId
        -PerfilParticipante perfil
        -double valor
        +getEventoId() int
        +setEventoId(int)
        +getPerfil() PerfilParticipante
        +setPerfil(PerfilParticipante)
        +getValor() double
        +setValor(double)
    }
    
    %% Classes DAO
    class UsuarioDAO {
        +salvar(Usuario) void
        +buscarPorId(int) Usuario
        +buscarPorEmail(String) Usuario
        +listarTodos() List~Usuario~
        +atualizar(Usuario) void
        +excluir(int) void
    }
    
    class EventoDAO {
        +salvar(Evento) void
        +buscarPorId(int) Evento
        +listarTodos() List~Evento~
        +listarPorAdministrador(int) List~Evento~
        +atualizar(Evento) void
        +excluir(int) void
    }
    
    class AtividadeDAO {
        +salvar(Atividade) void
        +buscarPorId(int) Atividade
        +listarPorEvento(int) List~Atividade~
        +listarTodas() List~Atividade~
        +atualizar(Atividade) void
        +excluir(int) void
        +contarInscritos(int) int
    }
    
    class InscricaoDAO {
        +salvar(Inscricao) void
        +buscarPorId(int) Inscricao
        +listarPorParticipante(int) List~Inscricao~
        +listarPorEvento(int) List~Inscricao~
        +listarPorAtividade(int) List~Inscricao~
        +listarTodas() List~Inscricao~
        +atualizar(Inscricao) void
        +excluir(int) void
        +verificarInscricaoExistente(int, int, int) boolean
    }
    
    class ValorInscricaoDAO {
        +salvar(ValorInscricao) void
        +buscarPorEventoEPerfil(int, PerfilParticipante) ValorInscricao
        +listarPorEvento(int) List~ValorInscricao~
        +atualizar(ValorInscricao) void
        +excluir(int, PerfilParticipante) void
    }
    
    %% Classes de Serviço
    class AutenticacaoService {
        -UsuarioDAO usuarioDAO
        +AutenticacaoService(UsuarioDAO)
        +autenticar(String, String) Usuario
        +registrar(String, String, String, TipoUsuario, PerfilParticipante) Usuario
    }
    
    class EventoService {
        -EventoDAO eventoDAO
        -ValorInscricaoDAO valorInscricaoDAO
        +EventoService(EventoDAO, ValorInscricaoDAO)
        +criarEvento(Evento) void
        +listarEventos() List~Evento~
        +buscarEvento(int) Evento
        +atualizarEvento(Evento) void
        +excluirEvento(int) void
        +definirValorInscricao(ValorInscricao) void
        +buscarValorInscricao(int, PerfilParticipante) ValorInscricao
    }
    
    class AtividadeService {
        -AtividadeDAO atividadeDAO
        +AtividadeService(AtividadeDAO)
        +criarAtividade(Atividade) void
        +listarAtividades(int) List~Atividade~
        +buscarAtividade(int) Atividade
        +atualizarAtividade(Atividade) void
        +excluirAtividade(int) void
        +verificarDisponibilidade(int) boolean
    }
    
    class InscricaoService {
        -InscricaoDAO inscricaoDAO
        -AtividadeDAO atividadeDAO
        +InscricaoService(InscricaoDAO, AtividadeDAO)
        +inscreverEmEvento(int, int, double) void
        +inscreverEmAtividade(int, int, int, double) void
        +listarInscricoesParticipante(int) List~Inscricao~
        +listarInscricoesEvento(int) List~Inscricao~
        +cancelarInscricao(int) void
    }
    
    class PagamentoService {
        +processarPagamento(Inscricao, double) void
        +confirmarPagamento(int) void
        +cancelarPagamento(int) void
    }
    
    class SessaoService {
        -Usuario usuarioAtual
        +getUsuarioAtual() Usuario
        +setUsuarioAtual(Usuario) void
        +fazerLogout() void
        +isLogado() boolean
    }
    
    class ServiceFactory {
        +getAutenticacaoService() AutenticacaoService
        +getEventoService() EventoService
        +getInscricaoService() InscricaoService
        +getAtividadeService() AtividadeService
    }
    
    %% Classes de Exceção
    class AutenticacaoException {
        +AutenticacaoException(String)
    }
    
    class DatabaseException {
        +DatabaseException(String)
    }
    
    class AtividadeLotadaException {
        +AtividadeLotadaException(String)
    }
    
    class InscricaoDuplicadaException {
        +InscricaoDuplicadaException(String)
    }
    
    class PagamentoInvalidoException {
        +PagamentoInvalidoException(String)
    }
    
    %% Classes de Interface
    class ConsoleInterface {
        +iniciar() void
        +exibirMenu() void
    }
    
    class Menu {
        +exibir() void
        +processarOpcao(int) void
    }
    
    class MenuLogin {
        +exibir() void
        +processarLogin() void
    }
    
    class MenuAdministrador {
        +exibir() void
        +gerenciarEventos() void
        +gerenciarAtividades() void
        +gerenciarUsuarios() void
        +visualizarInscricoes() void
        +gerenciarValoresInscricao() void
        +gerarRelatorios() void
    }
    
    class MenuParticipante {
        +exibir() void
        +visualizarEventos() void
        +inscreverEmEvento() void
        +inscreverEmAtividade() void
        +visualizarMinhasInscricoes() void
        +realizarPagamento() void
    }
    
    %% Relacionamentos
    Usuario <|-- Administrador
    Usuario <|-- Participante
    
    Evento ||--o{ Atividade : "contém"
    Evento ||--o{ Inscricao : "recebe"
    Evento ||--o{ ValorInscricao : "define"
    
    Atividade ||--o{ Inscricao : "recebe"
    
    Participante ||--o{ Inscricao : "faz"
    
    Evento ||--|| Administrador : "é gerenciado por"
    
    %% Relacionamentos com Enums
    Usuario --> TipoUsuario
    Usuario --> PerfilParticipante
    Atividade --> TipoAtividade
    Inscricao --> StatusPagamento
    ValorInscricao --> PerfilParticipante
    
    %% Relacionamentos de Serviços
    AutenticacaoService --> UsuarioDAO
    EventoService --> EventoDAO
    EventoService --> ValorInscricaoDAO
    AtividadeService --> AtividadeDAO
    InscricaoService --> InscricaoDAO
    InscricaoService --> AtividadeDAO
    
    %% Relacionamentos de Interface
    ConsoleInterface --> MenuLogin
    ConsoleInterface --> MenuAdministrador
    ConsoleInterface --> MenuParticipante
    Menu <|-- MenuLogin
    Menu <|-- MenuAdministrador
    Menu <|-- MenuParticipante
    
    %% Relacionamentos de Exceção
    AutenticacaoService ..> AutenticacaoException
    InscricaoService ..> AtividadeLotadaException
    InscricaoService ..> InscricaoDuplicadaException
    PagamentoService ..> PagamentoInvalidoException
    EventoService ..> DatabaseException
    AtividadeService ..> DatabaseException
    InscricaoService ..> DatabaseException
``` 