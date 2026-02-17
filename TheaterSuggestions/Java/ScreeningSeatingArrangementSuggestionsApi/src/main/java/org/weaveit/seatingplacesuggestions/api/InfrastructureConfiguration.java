package org.weaveit.seatingplacesuggestions.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository;
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider;
import org.weaveit.seatingplacesuggestions.AuditoriumSeatingArrangements;
import org.weaveit.seatingplacesuggestions.SeatingArrangementRecommender;

import java.io.IOException;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public AuditoriumSeatingArrangements auditoriumSeatingArrangements() throws IOException {
        return new FileBasedAuditoriumSeatingArrangements(
                new AuditoriumLayoutRepository(),
                new ReservationsProvider()
        );
    }

    @Bean
    public SeatingArrangementRecommender seatingArrangementRecommender(AuditoriumSeatingArrangements auditoriumSeatingArrangements) {
        return new SeatingArrangementRecommender(auditoriumSeatingArrangements);
    }
}
