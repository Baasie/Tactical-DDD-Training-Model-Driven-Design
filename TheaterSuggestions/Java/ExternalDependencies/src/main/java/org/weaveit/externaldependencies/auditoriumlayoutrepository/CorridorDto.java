package org.weaveit.externaldependencies.auditoriumlayoutrepository;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CorridorDto(@JsonProperty("Number") int number, @JsonProperty("InvolvedRowNames") Iterable<String> involvedRowNames) {

}
