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

- Configuração Android no arquivo [`app/build.gradle.kts`](app/build.gradle.kts)
- Activity principal registrada em [`app/src/main/AndroidManifest.xml`](app/src/main/AndroidManifest.xml)
- Entrada da aplicação em [`mycryptolog/application/MainActivity.kt`](mycryptolog/application/MainActivity.kt)

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

- Separação de responsabilidades em [`mycryptolog/presentation/viewmodel/WalletViewModel.kt`](mycryptolog/presentation/viewmodel/WalletViewModel.kt)
- Regras de negócio em [`mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt`](mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt)
- Mapeamento entre camadas em [`mycryptolog/data/mapper/Mappers.kt`](mycryptolog/data/mapper/Mappers.kt)
- Separação entre `HomeScreen` e `HomeContent` em [`mycryptolog/presentation/ui/screens/HomeScreen.kt`](mycryptolog/presentation/ui/screens/HomeScreen.kt)

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

- ViewModels em [`mycryptolog/presentation/viewmodel`](mycryptolog/presentation/viewmodel)
- Use cases em [`mycryptolog/domain/usecase`](mycryptolog/domain/usecase)
- Contratos de repositório em [`mycryptolog/domain/repository`](mycryptolog/domain/repository)
- Implementações de repositório em [`mycryptolog/data/repository`](mycryptolog/data/repository)

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

- Inicialização do Hilt em [`mycryptolog/MyCryptoLogApp.kt`](mycryptolog/MyCryptoLogApp.kt)
- Injeção na activity em [`mycryptolog/application/MainActivity.kt`](mycryptolog/application/MainActivity.kt)
- Provedores de dependência em [`mycryptolog/di/AppModule.kt`](mycryptolog/di/AppModule.kt)
- Injeção por construtor em [`mycryptolog/presentation/viewmodel/AuthViewModel.kt`](mycryptolog/presentation/viewmodel/AuthViewModel.kt), [`mycryptolog/presentation/viewmodel/WalletViewModel.kt`](mycryptolog/presentation/viewmodel/WalletViewModel.kt) e [`mycryptolog/presentation/viewmodel/TransactionViewModel.kt`](mycryptolog/presentation/viewmodel/TransactionViewModel.kt)

### 5. Testes Unitários

Atualmente o projeto contém 5 testes unitários no arquivo `CalculateHoldingsUseCaseTest`, cobrindo regras centrais da lógica de negócio:

1. soma correta de múltiplas compras;
2. subtração correta quando há venda;
3. separação de saldo por ativo;
4. remoção de ativo cujo saldo final é zero;
5. cálculo correto do preço médio de compra.

Esses testes validam o use case `CalculateHoldingsUseCase`, responsável por uma das regras mais importantes do aplicativo: o processamento do portfólio do usuário.

Evidências:

- Testes em [`mycryptolog/domain/usecase/CalculateHoldingsUseCaseTest.kt`](mycryptolog/domain/usecase/CalculateHoldingsUseCaseTest.kt)
- Regra testada em [`mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt`](mycryptolog/domain/usecase/CalculateHoldingsUseCase.kt)

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

- Repositórios em [`mycryptolog/domain/repository/WalletRepository.kt`](mycryptolog/domain/repository/WalletRepository.kt), [`mycryptolog/domain/repository/TransactionRepository.kt`](mycryptolog/domain/repository/TransactionRepository.kt), [`mycryptolog/data/repository/WalletRepositoryImpl.kt`](mycryptolog/data/repository/WalletRepositoryImpl.kt) e [`mycryptolog/data/repository/TransactionRepositoryImpl.kt`](mycryptolog/data/repository/TransactionRepositoryImpl.kt)
- Mapper em [`mycryptolog/data/mapper/Mappers.kt`](mycryptolog/data/mapper/Mappers.kt)
- Estados reativos em [`mycryptolog/presentation/viewmodel/AuthViewModel.kt`](mycryptolog/presentation/viewmodel/AuthViewModel.kt), [`mycryptolog/presentation/viewmodel/WalletViewModel.kt`](mycryptolog/presentation/viewmodel/WalletViewModel.kt) e [`mycryptolog/presentation/viewmodel/TransactionViewModel.kt`](mycryptolog/presentation/viewmodel/TransactionViewModel.kt)
- State Patter em em [`mycryptolog/presentation/viewmodel/WalletUiState.kt`](mycryptolog/presentation/viewmodel/WalletUiState.kt)

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

- Grafo de navegação em [`mycryptolog/presentation/navigation/NavGraph.kt`](mycryptolog/presentation/navigation/NavGraph.kt)
- Tela de login em [`mycryptolog/presentation/ui/screens/auth/LoginScreen.kt`](mycryptolog/presentation/ui/screens/auth/LoginScreen.kt)
- Tela de cadastro em [`mycryptolog/presentation/ui/screens/auth/SignUpScreen.kt`](mycryptolog/presentation/ui/screens/auth/SignUpScreen.kt)
- Tela principal com abas em [`mycryptolog/presentation/ui/screens/HomeScreen.kt`](mycryptolog/presentation/ui/screens/HomeScreen.kt)
- Tela de carteiras em [`mycryptolog/presentation/ui/screens/WalletsScreen.kt`](mycryptolog/presentation/ui/screens/WalletsScreen.kt)
- Tela de transações em [`mycryptolog/presentation/ui/screens/TransactionsScreen.kt`](mycryptolog/presentation/ui/screens/TransactionsScreen.kt)
- Tela de adicionar/editar transação em [`mycryptolog/presentation/ui/screens/AddTransactionScreen.kt`](mycryptolog/presentation/ui/screens/AddTransactionScreen.kt)

## Estrutura do Projeto

```text
mycryptolog
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
