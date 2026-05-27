package com.nutriscan.app.data.api

import java.io.IOException

/**
 * Exceção lançada quando o limite de requisições da API é atingido.
 *
 * Esta exceção é utilizada pelo [RateLimitInterceptor] para sinalizar
 * que o aplicativo fez muitas requisições em um curto período de tempo,
 * e que o usuário deve aguardar antes de tentar novamente.
 */
class RateLimitException(message: String) : IOException(message) {
    companion object {
        const val RATE_LIMIT_MESSAGE = "Muitas consultas em pouco tempo. Tente novamente em alguns instantes."
    }
}
