package services;

import dao.*;

public class ServiceFactory {
    public static AutenticacaoService getAutenticacaoService() {
        return new AutenticacaoService(new UsuarioDAO());
    }

    public static EventoService getEventoService() {
        return new EventoService(new EventoDAO(), new ValorInscricaoDAO());
    }

    public static InscricaoService getInscricaoService() {
        return new InscricaoService(new InscricaoDAO(), new AtividadeDAO());
    }

    public static AtividadeService getAtividadeService() {
        return new AtividadeService(new AtividadeDAO());
    }
}