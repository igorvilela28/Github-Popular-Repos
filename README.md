# Popular GitHub Repos

Este projeto tem como objetivo demonstrar algumas habilidades aprendidas para o desenvolvimento Android utilizando Kotlin.

No presente momento, o aplicativo é capaz de conectar-se a [API](https://developer.github.com/v3/) do GitHub e mostrar uma lista paginada dos repositórios que utilizam a linguagem Java. Ao clicar em algum repositório, você será levado a uma tela que contém os `pull requests` abertos para o repositório. Finalmente, ao clicar em algum pull request, você será levado (pelo navegador) a página do pull request em questão.

## Como utilizar este projeto

O projeto foi desenvolvido utilizando a versão do Android Studio 2.3, Gradle 2.3.3 e Kotlin 1.1.51. Recomenda-se instalar o [plugin Kotlin](https://kotlinlang.org/docs/tutorials/kotlin-android.html). Após instalar, o projeto deverá ser compilado normalmente. 

Observe que a partir da versão 3.0 do Android Studio, o suporte para Kotlin já estará incluso e você poderá ignorar a instalação do plugin.

## Casos de uso cobertos

O projeto seguiu os requisitos descritos [aqui](https://github.com/appprova/desafio-mobile-android/blob/master/README.md). Entretanto, alguns requisitos não foram possíveis de serem cumpridos, sendo eles:

- `Mostrar nome do autor` : No mockup podemos observar que nas listagens é mostrado além do login do usuário, também o seu nome. Essa informação não estava disponível em nenhuma das chamadas da API e portanto não foi mostrada nas telas.
- `Contagem de pull requests abertos e fechados`: Novamente no mockup, podemos observar que na tela de listagem dos pull requests mostra uma contagem dos abertos / fechados. Entretanto, a [chamada](https://developer.github.com/v3/pulls/#list-pull-requests) para recuperar os dados não contém tal informação.

Devido à não especificação de quais pull requests seriam mostrados, foi decidido que seria mostrado apenas aqueles que estavam com o status `open`. Entretanto, diferente de Java puro, Kotlin permite a utilização de valores padrões na definição de métodos. Devido a isso, será fácil modificar o comportamento da tela. Ou ainda, após as modificações necessárias na API, ter botões para listagem como apresentado no mockup.

## Organização do projeto

A arquitetura do projeto segue os conceitos da [Clean architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) e é utilizado o Model-View-Presenter para a camada de apresentação.

Ainda que estejamos seguindo o clean architecture, por questões de simplicidade, as mesmas entidades foram utilizadas em todas as camadas da aplicação. Visto que não eram necessárias alterações ou transformações dos dados.

A estrutura em módulos do projeto é a seguinte:

- `data`: Contém os repositórios para acesso aos dados
- `dependency_injection`: configuração dos módulos e componentes do Dagger na versão 2.11 para injeção de dependências
- `domain`: Contém os Interactors (também conhecidos como UseCases) para a interação `presenters-entities`. Também contém as data classes para representação dos dados.
- `network`: Responsável pela comunicação via rede do app.
- `presentation`: Contém as telas do aplicativo com os seus respectivos presenters.

## Bibliotecas e frameworks utilizados

- [Retrofit](http://square.github.io/retrofit/): para as chamadas REST.
- [Dagger2](https://github.com/google/dagger): para injeção de dependência.
- [Timber](https://github.com/JakeWharton/timber): para logs
- [Picasso](https://github.com/square/picasso): para loading de imagens.
- [Gson](https://github.com/google/gson): para mapeamento de JSON -> objeto
- [Android Support Libraries](https://developer.android.com/topic/libraries/support-library/index.html): para implementação da UI das telas

## Decisões de implementação

A utilização do Kotlin foi decidida por ser uma nova alternativa para o desenvolvimento Android nativo. Além de ser menos verbosa que o Java, Kotlin nos oferece diversas ferramentas interessantes, como [null safety](https://kotlinlang.org/docs/reference/null-safety.html), [high order functions e lambdas](https://kotlinlang.org/docs/reference/lambdas.html), [delegate properties](https://kotlinlang.org/docs/reference/delegated-properties.html), [extensions](https://kotlinlang.org/docs/reference/extensions.html) e ainda em versão experimental [coroutines](https://github.com/Kotlin/kotlinx.coroutines).

Programação assíncrona é uma realidade para o desenvolvimento Android. O fato de que utilizando coroutines podemos escrever trechos de código assíncronos sequencialmente me chamou bastante a atenção. Portanto, coroutines foram utilizadas como prova de conceito.

De acordo com a [documentação](https://kotlinlang.org/docs/reference/coroutines.html) uma coroutine é uma computação que é capaz de suspender/resumir sua execução, sem bloquear uma thread, o que encaixa perfeitamente com o desenvolvimento Android, visto que precisamos executar certas operações fora da UI Thread (como chamadas de rede, acesso ao banco de dados local, etc).

Sendo assim,foi possível escrever a interação `view <--> presenter <--> interactor <--> repository` de forma simples e direta. Por exemplo, em nossa `PopularReposActivity`, temos o seguinte treco de código:

```kotlin
    mLoadRepositoriesJob = launch(UI) {
            mPresenter.loadRepositories(mItems.size)
        }

```
Observe que estamos iniciando uma nova coroutine e utilizando o [dispatcher](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#dispatchers-and-threads) `UI`, que indica que a execução da coroutine será na UI Thread. A função `loadRepositories` é uma `suspend function`, ou seja uma função que pode suspender sua execução e na sua implementação temos: 

```kotlin
    ...
    val params = LoadGitHubReposInteractor.Params(currentItemsSize)
    val repos = mLoadGitHubReposInteractor.execute(params).await()
    mView?.showRepositories(repos)
    ...
```
Note que estamos fazendo chamadas sequenciais do código, tornando sua intenção clara e direta. Essa foi a maior vantagem notada ao utilizar coroutines: Não precisamos lidar com callbacks ou trechos de código complexos para lidar com assíncronismo.

O segredo para o acesso aos dados não ser executado na UI Thread se dá na implementação do método `execute` de nosso Interactor, que inicia uma nova coroutine com o builder [async](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#concurrent-using-async) junto com o dispatcher CommonPool, que garante que a execução será feita em outra thread. Ao finalizar a execução, seja com sucesso ou erro, a coroutine iniciada pelo interactor será resumida na coroutine que o chamou, assim temos as chamadas assíncronas escritas de forma sequencial, além de não precisarmos nos preocupar com atualização das views na  UI Thread, visto que a coroutine do presenter está sendo executada na própria UI Thread.

```kotlin
suspend override fun execute(params: Params?): Deferred<List<GitHubRepo>> = async(CommonPool) {

        if(params != null) {

            val page = getPageToSearch(params)
            gitHubRepository.loadRepositories(page = page)

        } else {

            gitHubRepository.loadRepositories()
        }
    }
```

## Trabalho futuro

Ambas nossas features possuem implementações semelhantes, portanto poderiamos ter feito as seguintes abstrações:
- Generalizar Adapter para as listas das telas
- Generalizar Activities das telas.

Testes unitários e de integração devem ser adicionados no futuro.

Foi observado também uma queda de performance ao carregar itens nas listas. O problema está ligado ao carregamento das imagens dos avatares e deve ser consertado em versões futuras.

Entretanto, devido ao prazo limitado, optei por entregar um produto funcional, ainda que com possibilidades de melhoras.

# Licença

    Copyright 2017 Igor Vilela

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
