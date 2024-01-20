package io.js.domain;

import java.util.UUID;

public record Train(
        UUID id,
        int carriages,
        Status status,
        long mileage
) {
    public static Train withId(UUID id, Train train) {
        return new Train(id, train.carriages(), train.status(), train.mileage());
    }
}