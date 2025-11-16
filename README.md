# My Crypto Log - Aplicativo de Portfólio de Criptomoedas

My Crypto Log é um aplicativo Android moderno, desenvolvido com Jetpack Compose, que permite aos usuários registrar e acompanhar suas operações de compra e venda de criptomoedas. O aplicativo utiliza Firebase como Backend as a Service (MBaaS) para fornecer autenticação segura, armazenamento de dados em tempo real e proteção contra abusos.

## 📋 Funcionalidades

- **Autenticação de Usuários:** Sistema completo de Login, Registro, "Esqueci a Senha" e Logout.
- **Sessão Persistente:** O usuário continua logado mesmo após fechar o aplicativo.
- **Gerenciamento de Múltiplas Carteiras:** Crie e gerencie diferentes carteiras (ex: "Binance", "Carteira Pessoal").
- **Registro de Transações:** Adicione transações de compra (BUY) e venda (SELL) com detalhes como cripto, quantidade, preço e data.
- **Edição e Exclusão:** Gerencie transações existentes com opções de editar e deletar (com diálogo de confirmação).
- **Cálculos de Portfólio:** A tela de carteiras calcula e exibe automaticamente:
  - **Preço Médio Ponderado** de compra para cada ativo.
  - **Valor Líquido Investido** (`Total Gasto em Compras - Total Recebido em Vendas`).
- **UI Moderna e Reativa:** Interface construída com Jetpack Compose e Material 3, incluindo:
  - Navegação por Abas (Wallets e Transactions).
  - App Bars flexíveis que reagem à rolagem.
  - Componentes de seleção de data e menus dropdown pesquisáveis.

## 🛠️ Tecnologias e Arquitetura

- **Linguagem:** 100% Kotlin
- **UI:** Jetpack Compose com Material 3
- **Arquitetura:**
  - **MVVM (Model-View-ViewModel):** Separação clara da lógica de negócio e da UI.
  - **Stateful/Stateless Composables:** Adoção do padrão de componentização do Compose, onde os componentes de UI (Stateless) são separados dos componentes que gerenciam o estado (Stateful).
- **Navegação:** Jetpack Navigation Compose com grafos aninhados para um controle robusto do back stack.
- **Backend (MBaaS - Firebase):**
  - **Firebase Authentication:** Para todo o fluxo de gerenciamento de usuários.
  - **Firebase Realtime Database:** Para armazenamento de dados das carteiras e transações em tempo real.
  - **Firebase App Check (com Play Integrity):** Para ajudar a proteger os back-ends do app contra abusos, impedindo que clientes não autorizados acessem seus recursos de back-end. Ele funciona com os Serviços do Google (incluindo o Firebase e o Google Cloud) e com seus próprios back-ends personalizados para manter seus recursos seguros.

## 🚀 Guia de Configuração e Instalação

Para clonar e executar este projeto em seu ambiente local, siga os passos abaixo.

### Pré-requisitos

- Android Studio (versão mais recente recomendada)
- JDK 11 ou superior (verifique a configuração do Gradle JDK no Android Studio)

### Passo 1: Configurar o Projeto no Firebase (Se necessário)

1.  Crie um novo projeto no [Console do Firebase](https://console.firebase.google.com/).
2.  Adicione um novo aplicativo Android ao seu projeto com o **package name** exato: `com.blimas.mycryptolog`.
3.  Siga as instruções para baixar o arquivo **`google-services.json`** e coloque-o dentro do diretório **`app/`** do projeto.
4.  No Console do Firebase, vá para a seção **Authentication** -> **Sign-in method** e ative o provedor **"E-mail/senha"**.

### Passo 2: Gerar e Adicionar a Chave SHA-1

Para que o Firebase Authentication funcione corretamente (especialmente o Login e o "Esqueci a Senha"), você precisa registrar a impressão digital do seu ambiente de desenvolvimento.

1.  No Android Studio, abra a aba **Gradle** (geralmente no canto direito).
2.  Navegue até **MyCryptoLog -> app -> Tasks -> android** e dê um duplo clique em **`signingReport`**.
3.  No painel "Run" que aparecer, encontre a chave **SHA-1** da variante `debug`.
4.  Copie a chave SHA-1.
5.  No Console do Firebase, vá para **Configurações do Projeto** (⚙️) -> **Geral**.
6.  Role para baixo até "Seus aplicativos", selecione seu app Android e clique em **Adicionar impressão digital**. Cole a chave SHA-1 que você copiou.

### Passo 3: Configurar o App Check

O projeto usa o App Check para segurança. Para rodar em um emulador ou dispositivo de teste, você precisa registrar o token de depuração.

1.  No Console do Firebase, vá para a seção **App Check**.
2.  Selecione seu aplicativo e, na aba "Apps", clique em **Play Integrity** para registrar o provedor.
3.  Execute o aplicativo no seu emulador/dispositivo.
4.  No Android Studio, abra a janela do **Logcat**.
5.  Na barra de busca do Logcat, filtre por **`FirebaseAppCheck`**.
6.  Você verá uma mensagem de depuração contendo o token:
    ```
    D/FirebaseAppCheck: Enter this debug token in the Firebase Console: [COPIE_ESTE_TOKEN_LONGO]
    ```
7.  Copie o token.
8.  Volte à seção **App Check** no Console do Firebase, clique no menu de três pontos (⋮) ao lado do seu app e selecione **Gerenciar tokens de depuração**.
9.  Clique em **Adicionar token de depuração** e cole o token que você copiou.

Após seguir estes três passos, o projeto estará 100% funcional e pronto para ser executado.
