package io.js.domain;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BrowserCallable
@AnonymousAllowed
@Service
public class TrainService {
    private final Map<UUID, Train> trains;

    public TrainService() {
        this.trains = generateRandomTrains();
    }

    public Train createTrain(Train train) {
        if (train.id() != null) {
            return updateTrain(train);
        }
        var uuid = UUID.randomUUID();
        var trainWithId = Train.withId(uuid, train);
        trains.put(uuid, trainWithId);
        return trainWithId;
    }


    public Train updateTrain(Train train) {
        UUID id = train.id();

        boolean trainExists = trains.values().stream()
                .anyMatch(tr -> tr.id().equals(id));

        System.out.println("Train exists: " + trainExists);

        if (!trainExists) {
            throw new IllegalArgumentException("Train with ID = [" + train.id() + "] not exists");
        }

        var updatedTrain = Train.withId(train.id(), train);
        trains.put(train.id(), updatedTrain);
        return updatedTrain;
    }


    public Train getTrain(UUID id) {
        return this.trains.get(id);
    }

    public void deleteTrain(UUID id) {
        this.trains.remove(id);
    }

    public List<Train> list() {
        return new ArrayList<>(this.trains.values());
    }

    private Map<UUID, Train> generateRandomTrains() {
        return IntStream.range(0, 10)
                .boxed()
                .collect(Collectors.toMap(
                        index -> UUID.randomUUID(),
                        this::createRandomTrain
                ));
    }

    private Train createRandomTrain(int index) {
        UUID id = UUID.randomUUID();
        int number = index + 10;
        Status status = index % 2 == 0 ? Status.MAINTENANCE : Status.ACTIVE;
        int capacity = index + 1000;

        return new Train(id, number, status, capacity);
    }

}