```mermaid
usecaseDiagram
  actor "Administrador" as Admin
  actor "Participante" as User

  Admin --> (Gerenciar eventos)
  Admin --> (Gerenciar atividades)
  Admin --> (Gerenciar usuários)
  Admin --> (Visualizar inscrições)
  Admin --> (Gerenciar valores de inscrição)
  Admin --> (Relatórios)
  Admin --> (Fazer logout)

  User --> (Visualizar eventos disponíveis)
  User --> (Visualizar atividades de um evento)
  User --> (Inscrever-se em evento)
  User --> (Inscrever-se em atividade específica)
  User --> (Visualizar minhas inscrições)
  User --> (Realizar pagamento)
  User --> (Fazer logout)
```

---

## Menus e Submenus

### Administrador
- **Menu Principal**
  - Gerenciar eventos
    - Criar novo evento
    - Listar eventos
    - Visualizar atividades de um evento
    - Editar evento
    - Excluir evento
  - Gerenciar atividades
    - Criar nova atividade
    - Listar atividades
    - Editar atividade
    - Excluir atividade
  - Gerenciar usuários
    - Listar usuários
    - Editar usuário
    - Excluir usuário
    - Criar novo usuário
  - Visualizar inscrições
    - Listar todas as inscrições
  - Gerenciar valores de inscrição
    - Definir/editar valores
    - Listar valores
  - Relatórios
    - Relatório de inscrições
    - Relatório financeiro
  - Fazer logout

### Participante
- **Menu Principal**
  - Visualizar eventos disponíveis
  - Visualizar atividades de um evento
  - Inscrever-se em evento
  - Inscrever-se em atividade específica
  - Visualizar minhas inscrições
  - Realizar pagamento
  - Fazer logout 