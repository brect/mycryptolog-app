# My Crypto Log

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose para registrar, organizar e acompanhar operações de compra e venda de criptomoedas em diferentes carteiras.

## Visão Geral do Projeto

O aplicativo permite:

- autenticação de usuários com Firebase Authentication;
- criação e gerenciamento de múltiplas carteiras;
- cadastro, edição e exclusão de transações de compra e venda;
- cálculo automático de quantidade atual, preço médio e valor líquido investido por ativo;
- navegação entre telas de autenticação, listagem e cadastro.

## Tecnologias Utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Firebase Authentication
- Firebase Realtime Database
- Firebase App Check
- Hilt para injeção de dependência
- JUnit para testes unitários

## Relatório de Requisitos

### 1. Desenvolvimento de um aplicativo Android

O projeto é um aplicativo Android nativo configurado no módulo `app`, com `applicationId`, `minSdk`, `targetSdk` e activity principal definidos em Gradle e Manifest.

Evidências:

- Configuração Android no arquivo [`app/build.gradle.kts`](https://github.com/brect/mycryptolog-app/blob/main/app/build.gradle.kts)
- Activity principal registrada em [`app/src/main/AndroidManifest.xml`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/AndroidManifest.xml)
- Entrada da aplicação em [`app/src/main/java/com/blimas/mycryptolog/application/MainActivity.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/application/MainActivity.kt)

### 2. Clean Code

O projeto segue princípios básicos de código limpo com foco em legibilidade, separação de responsabilidades e facilidade de manutenção.

Como isso foi aplicado:

- Organização em camadas e pacotes específicos:
  - `data`: acesso a dados, DTOs, repositórios e mapeamentos.
  - `domain`: modelos, contratos de repositório e regras de negócio.
  - `presentation`: telas, navegação, componentes visuais e `ViewModel`s.
- Nomes de classes e métodos são descritivos, como `CalculateHoldingsUseCase`, `TransactionRepositoryImpl`, `AddTransactionScreen` e `sendPasswordResetEmail`.
- Regras de negócio foram extraídas da interface para use cases específicos, evitando concentrar lógica diretamente nas telas.
- A conversão entre modelos de domínio e modelos persistidos foi isolada em mappers, reduzindo acoplamento entre camadas.
- Em Compose, o projeto separa composables stateful e stateless em pontos importantes, melhorando reutilização e testabilidade.

Evidências:

- Separação de responsabilidades em [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletViewModel.kt)
- Regras de negócio em [`app/src/main/java/com/blimas/mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt)
- Mapeamento entre camadas em [`app/src/main/java/com/blimas/mycryptolog/data/mapper/Mappers.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/data/mapper/Mappers.kt)
- Separação entre `HomeScreen` e `HomeContent` em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/HomeScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/HomeScreen.kt)

### 3. Arquitetura de Software

O projeto utiliza o padrão arquitetural MVVM.

Estrutura observada:

- Model:
  - classes do domínio como `Wallet`, `Transaction`, `ProcessedHolding` e `AuthState`;
  - contratos de repositório e casos de uso da camada `domain`.
- View:
  - telas Compose como `LoginScreen`, `SignUpScreen`, `HomeScreen`, `WalletsScreen`, `TransactionsScreen` e `AddTransactionScreen`.
- ViewModel:
  - `AuthViewModel`, `WalletViewModel`, `TransactionViewModel` e `CryptoViewModel`.

Fluxo arquitetural:

1. A View captura ações do usuário.
2. A View delega a ação para o ViewModel.
3. O ViewModel usa use cases.
4. Os use cases dependem de interfaces de repositório.
5. As implementações dos repositórios acessam o Firebase.
6. O resultado volta para a View por meio de `LiveData` e `StateFlow`.

Evidências:

- ViewModels em [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel`](https://github.com/brect/mycryptolog-app/tree/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel)
- Use cases em [`app/src/main/java/com/blimas/mycryptolog/domain/usecase`](https://github.com/brect/mycryptolog-app/tree/main/app/src/main/java/com/blimas/mycryptolog/domain/usecase)
- Contratos de repositório em [`app/src/main/java/com/blimas/mycryptolog/domain/repository`](https://github.com/brect/mycryptolog-app/tree/main/app/src/main/java/com/blimas/mycryptolog/domain/repository)
- Implementações de repositório em [`app/src/main/java/com/blimas/mycryptolog/data/repository`](https://github.com/brect/mycryptolog-app/tree/main/app/src/main/java/com/blimas/mycryptolog/data/repository)

### 4. Injeção de Dependência

O projeto implementa Dependency Injection com Hilt.

Como foi atendido:

- A classe de aplicação está anotada com `@HiltAndroidApp`, habilitando Hilt no app.
- A `MainActivity` está anotada com `@AndroidEntryPoint`.
- O módulo `AppModule` fornece instâncias únicas de `FirebaseAuth`, `DatabaseReference` e das implementações de repositório.
- Os `ViewModel`s recebem suas dependências por construtor com `@Inject` e `@HiltViewModel`.

Benefícios alcançados:

- menor acoplamento entre classes;
- facilidade de substituição de dependências;
- melhor testabilidade;
- centralização da criação de objetos.

Evidências:

- Inicialização do Hilt em [`app/src/main/java/com/blimas/mycryptolog/MyCryptoLogApp.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/MyCryptoLogApp.kt)
- Injeção na activity em [`app/src/main/java/com/blimas/mycryptolog/application/MainActivity.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/application/MainActivity.kt)
- Provedores de dependência em [`app/src/main/java/com/blimas/mycryptolog/di/AppModule.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/di/AppModule.kt)
- Injeção por construtor em [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/AuthViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/AuthViewModel.kt), [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletViewModel.kt) e [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/TransactionViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/TransactionViewModel.kt)

### 5. Testes Unitários

Atualmente o projeto contém 5 testes unitários no arquivo `CalculateHoldingsUseCaseTest`, cobrindo regras centrais da lógica de negócio:

1. soma correta de múltiplas compras;
2. subtração correta quando há venda;
3. separação de saldo por ativo;
4. remoção de ativo cujo saldo final é zero;
5. cálculo correto do preço médio de compra.

Esses testes validam o use case `CalculateHoldingsUseCase`, responsável por uma das regras mais importantes do aplicativo: o processamento do portfólio do usuário.

Evidências:

- Testes em [`app/src/test/java/com/blimas/mycryptolog/domain/usecase/CalculateHoldingsUseCaseTest.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/test/java/com/blimas/mycryptolog/domain/usecase/CalculateHoldingsUseCaseTest.kt)
- Regra testada em [`app/src/main/java/com/blimas/mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt)

### 6. Design Patterns

O projeto utiliza padrões de projeto adequados ao contexto da aplicação.

- MVVM:
  - organiza a interação entre interface, estado e lógica de negócio.
- Repository Pattern:
  - as interfaces `WalletRepository` e `TransactionRepository` abstraem a fonte de dados;
  - as classes `WalletRepositoryImpl` e `TransactionRepositoryImpl` implementam o acesso ao Firebase.
- Dependency Injection:
  - com Hilt, para desacoplar criação e consumo de dependências.
- Observer / Reactive State:
  - uso de `LiveData` e `StateFlow` para atualização reativa da interface.
- Mapper Pattern:
  - conversão explícita entre objetos de persistência (`Dto`) e objetos de domínio.
- Singleton:
  - dependências providas com escopo `@Singleton` no módulo do Hilt.

Evidências:

- Repositórios em [`app/src/main/java/com/blimas/mycryptolog/domain/repository/WalletRepository.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/domain/repository/WalletRepository.kt), [`app/src/main/java/com/blimas/mycryptolog/domain/repository/TransactionRepository.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/domain/repository/TransactionRepository.kt), [`app/src/main/java/com/blimas/mycryptolog/data/repository/WalletRepositoryImpl.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/data/repository/WalletRepositoryImpl.kt) e [`app/src/main/java/com/blimas/mycryptolog/data/repository/TransactionRepositoryImpl.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/data/repository/TransactionRepositoryImpl.kt)
- Mapper em [`app/src/main/java/com/blimas/mycryptolog/data/mapper/Mappers.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/data/mapper/Mappers.kt)
- Estados reativos em [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/AuthViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/AuthViewModel.kt), [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletViewModel.kt) e [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/TransactionViewModel.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/TransactionViewModel.kt)
- State Pattern em [`app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletUiState.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/viewmodel/WalletUiState.kt)

### 7. Interface com pelo menos 3 telas funcionais

O aplicativo possui mais de 3 telas funcionais e navegáveis.

Telas:

1. Login
2. Cadastro de usuário
3. Home
4. Wallets
5. Transactions
6. Add/Edit Transaction

Além das telas principais, o app também possui diálogos funcionais de apoio, como redefinição de senha, criação de carteira, edição de carteira e confirmação de exclusão.

Como a navegação funciona:

- `NavGraph` decide entre fluxo autenticado e não autenticado.
- O grafo de autenticação contém as telas de login e cadastro.
- O grafo principal contém a tela inicial e a tela de cadastro/edição de transações.
- Dentro de `HomeScreen`, há navegação por abas entre `WalletsScreen` e `TransactionsScreen`.

Evidências:

- Grafo de navegação em [`app/src/main/java/com/blimas/mycryptolog/presentation/navigation/NavGraph.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/navigation/NavGraph.kt)
- Tela de login em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/auth/LoginScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/auth/LoginScreen.kt)
- Tela de cadastro em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/auth/SignUpScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/auth/SignUpScreen.kt)
- Tela principal com abas em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/HomeScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/HomeScreen.kt)
- Tela de carteiras em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/WalletsScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/WalletsScreen.kt)
- Tela de transações em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/TransactionsScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/TransactionsScreen.kt)
- Tela de adicionar/editar transação em [`app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/AddTransactionScreen.kt`](https://github.com/brect/mycryptolog-app/blob/main/app/src/main/java/com/blimas/mycryptolog/presentation/ui/screens/AddTransactionScreen.kt)

## Estrutura do Projeto

```text
app/src/main/java/com/blimas/mycryptolog
├── application        -> Activity principal
├── data               -> DTOs, mappers e repositórios concretos
├── di                 -> Módulos de injeção de dependência
├── domain             -> modelos, contratos e casos de uso
└── presentation       -> navegação, telas, componentes e ViewModels
```

## Guia de Configuração e Instalação

### Pré-requisitos

- Android Studio
- JDK 11 ou superior

### Passo 1: Configurar o Firebase

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com/).
2. Adicione um aplicativo Android com o package `com.blimas.mycryptolog`.
3. Baixe o arquivo `google-services.json`.
4. Coloque o arquivo dentro da pasta `app/`.
5. Ative o provedor `E-mail/senha` no Firebase Authentication.

### Passo 2: Configurar a SHA-1

1. No Android Studio, execute a task `signingReport`.
2. Copie a SHA-1 da variante `debug`.
3. Adicione essa impressão digital nas configurações do app Android dentro do Firebase.

### Passo 3: Configurar o App Check

1. Abra a seção App Check no Firebase.
2. Registre o provedor Play Integrity.
3. Execute o app.
4. Copie o token de depuração exibido no Logcat ao filtrar por `FirebaseAppCheck`.
5. Cadastre o token no console do Firebase em `Gerenciar tokens de depuração`.

Após essa configuração, o projeto estará pronto para execução no Android Studio.
