# Overview

O objetivo deste projeto é desenvolver um aplicativo Android usando Kotlin e Jetpack Compose seguindo a arquitetura MVVM. O aplicativo fornecerá funcionalidades para gerenciar uma lista de itens com as seguintes ações: Inserir, Editar, Desativar e Ativar. Cada item terá atributos incluindo Nome, Data de Nascimento, CPF, Cidade, Foto e Status. A tela principal exibirá uma lista de itens ativos, permitindo que os usuários realizem essas ações diretamente da tela principal. Além disso, haverá uma tela separada para listar itens inativos e reativá-los.

## Fases de Desenvolvimento:

1. **Configuração do Projeto:**
   - Criar um novo projeto Android no Android Studio.
   - Configurar o projeto para usar Kotlin como linguagem principal.
   - Configurar as dependências necessárias para o Jetpack Compose.

2. **Design da Interface de Usuário (UI):**
   - Projetar o layout da interface de usuário para a tela principal para exibir a lista de itens ativos.
   - Projetar o layout da interface de usuário para as telas de inserir, editar e desativar itens.
   - Projetar o layout da interface de usuário para a tela para exibir itens inativos e reativá-los.

3. **Modelo de Dados:**
   - Definir o modelo de dados para os itens, incluindo atributos como Nome, Data de Nascimento, CPF, Cidade, Foto e Status Ativo.

4. **Implementação do ViewModel:**
   - Criar classes ViewModel para lidar com a lógica de negócios para cada tela.
   - Implementar métodos nas classes ViewModel para realizar ações como inserir, editar, desativar e ativar itens.

5. **Configuração do Banco de Dados:**
   - Configurar um banco de dados local usando a Room Persistence Library para armazenar os itens.
   - Definir a interface DAO (Data Access Object) para realizar operações no banco de dados.

6. **Camada de Repositório:**
   - Implementar classes de repositório para atuar como uma camada de abstração entre o ViewModel e o banco de dados.
   - Implementar métodos nas classes de repositório para interagir com o banco de dados e realizar operações CRUD.

7. **Implementação da Interface de Usuário (UI):**
   - Implementar os componentes da interface de usuário usando Jetpack Compose para cada tela.
   - Vincular elementos da interface de usuário às classes ViewModel para observar as mudanças nos dados e atualizar a interface de usuário conforme necessário.
   - Implementar navegação entre telas usando o Jetpack Navigation Component.

## Ferramentas Utilizadas:

- **Android Studio:** Ambiente de Desenvolvimento Integrado (IDE) para desenvolvimento de aplicativos Android.
- **Kotlin:** Linguagem de programação principal usada para desenvolvimento de aplicativos Android.
- **Jetpack Compose:** Conjunto de ferramentas moderno para construir interfaces de usuário nativas para Android usando Kotlin.
- **Arquitetura MVVM:** Padrão de design usado para separar as preocupações de apresentação de dados e lógica de negócios.
- **Room Persistence Library:** Parte do Android Jetpack, fornece uma camada de abstração sobre o SQLite para permitir acesso mais robusto ao banco de dados.
- **Jetpack Navigation Component:** Biblioteca do Android Jetpack que ajuda a gerenciar a navegação entre telas.

## Descrição Detalhada do Aplicativo:

O aplicativo permite que os usuários gerenciem uma lista de itens com os seguintes atributos: Nome, Data de Nascimento, CPF (ID), Cidade, Foto e Status Ativo. A tela principal exibe uma lista de itens ativos, mostrando a foto, nome e idade de cada item. A partir desta tela, os usuários podem realizar as seguintes ações:

- **Inserir:** Os usuários podem adicionar um novo item navegando para uma tela separada e fornecendo as informações necessárias.
- **Editar:** Os usuários podem editar os detalhes de um item existente selecionando-o na lista e navegando para uma tela de edição separada.
- **Desativar:** Os usuários podem desativar um item ativo diretamente da tela principal alternando um interruptor ou selecionando uma opção de desativar.
- **Ativar:** Os usuários podem reativar itens inativos em uma tela separada listando os itens inativos.

O aplicativo segue a arquitetura MVVM, onde as classes ViewModel lidam com a lógica de negócios para cada tela, e as classes Repository atuam como uma camada de abstração entre o ViewModel e o banco de dados local. O banco de dados local é implementado usando a Room Persistence Library, fornecendo uma solução de armazenamento confiável para os dados dos itens.

No geral, o aplicativo fornece uma interface amigável para gerenciar uma lista de itens, permitindo que os usuários realizem várias ações de forma fácil, seguindo as melhores práticas modernas de desenvolvimento Android.
