# NutriScan

App Android nativo que escaneia códigos de barras de alimentos e recomenda opções mais nutritivas, com alertas para diabéticos.

## Funcionalidades

- **Scanner de código de barras** — leitura pela câmera usando ML Kit do Google
- **Busca manual** — pesquisar por nome do produto ou código de barras
- **Avaliação nutricional** — nota do produto (Nutri-Score) com dados gerais do alimento vindos da API
- **Alertas para diabéticos** — indicação visual de risco baseada em nutrientes (regra a definir)
- **Recomendação de alternativas** — sugere produtos mais saudáveis na mesma categoria
- **Histórico de scans** — lista de produtos consultados anteriormente, salva localmente

## Telas

| Tela | Descrição |
|------|-----------|
| **Home** | Campo de busca (nome ou código de barras) + botão para abrir scanner |
| **Scanner** | Câmera com leitura de código de barras em tempo real |
| **Resultado** | Dados nutricionais, nota, alertas diabéticos e alternativas recomendadas |
| **Histórico** | Lista de produtos escaneados, persistidos localmente |

## Stack Técnica

- **Linguagem:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Scanner:** ML Kit Barcode Scanning (Google)
- **API:** [Open Food Facts](https://world.openfoodfacts.org/)
- **Banco local:** Room (SQLite)
- **Arquitetura:** MVVM
- **Mínimo SDK:** 27 (Android 8.1)
- **Target SDK:** 36

## Offline

O app funciona parcialmente offline:
- **Com internet:** busca de produtos, scanner, recomendações
- **Sem internet:** histórico de scans salvos localmente

## API — Open Food Facts

API pública e gratuita, sem autenticação necessária.

- **Buscar por código de barras:** `GET https://world.openfoodfacts.org/api/v0/product/{barcode}.json`
- **Buscar por nome:** `GET https://world.openfoodfacts.org/cgi/search.pl?search_terms={query}&json=1`

### Dados utilizados da API

- Nome do produto e marca
- Tabela nutricional (calorias, açúcar, carboidratos, gordura, sódio, fibras, proteínas)
- Nutri-Score (nota de A a E)
- NOVA group (processamento do alimento)
- Categoria do produto (para buscar alternativas)
- URL da imagem do produto

## Design

- **Tema escuro** como padrão
- **Minimalista e limpo** — foco nos dados
- **Cards com cores** indicando qualidade nutricional (regras visuais a definir)

## Regras de Negócio

> **Pendente de definição:** fórmula/reenegra de avaliação nutricional e alertas para diabéticos será definida em iteração futura.

## Como rodar

```bash
cd app/NutriScan
./gradlew assembleDebug
```

Ou instalar diretamente em um dispositivo/emulador:

```bash
cd app/NutriScan
./gradlew installDebug
```

Requisitos:
- Android Studio (Iguana ou superior)
- Dispositivo ou emulador com Android 8.1+ (API 27)
- Conexão com internet para buscas na API
