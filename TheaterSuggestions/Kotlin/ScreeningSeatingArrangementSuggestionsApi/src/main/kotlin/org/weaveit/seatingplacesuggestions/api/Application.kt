package org.weaveit.seatingplacesuggestions.api

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider
import org.weaveit.seatingplacesuggestions.*

fun main() {
    val auditoriumSeatingArrangements: AuditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
        AuditoriumLayoutRepository(),
        ReservationsProvider()
    )
    val recommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson()
        }
        routing {
            get("/api/suggestions") {
                val showId = call.request.queryParameters["showId"]
                val partySize = call.request.queryParameters["partySize"]?.toIntOrNull()

                if (showId == null || partySize == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing showId or partySize")
                    return@get
                }

                val suggestions = recommender.makeSuggestions(showId, partySize)

                call.respond(toResponse(suggestions))
            }
        }
    }.start(wait = true)
}

private fun toResponse(suggestions: SuggestionsAreMade): Map<String, Any> {
    val suggestionsMap = linkedMapOf<String, List<String>>()
    for (category in PricingCategory.entries) {
        suggestionsMap[category.name] = suggestions.seatNames(category)
    }

    return linkedMapOf(
        "showId" to suggestions.showId,
        "partySize" to suggestions.partyRequested,
        "suggestions" to suggestionsMap
    )
}
