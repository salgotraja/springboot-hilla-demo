package io.js.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrainServiceTest {

    private TrainService trainService;

    @BeforeEach
    void setUp(){
        trainService = new TrainService();
    }

    @Test
    void createTrain_NewTrain_ShouldAddTrainWithId() {
        Train newTrain = new Train(null, 20, Status.ACTIVE, 1500);
        Train createdTrain = trainService.createTrain(newTrain);
        assertThat(createdTrain.id()).isNotNull();
        assertThat(newTrain.carriages()).isEqualTo(createdTrain.carriages());
        assertThat(newTrain.status()).isEqualTo(createdTrain.status());
        assertThat(newTrain.mileage()).isEqualTo(createdTrain.mileage());

        Train retrievedTrain = trainService.getTrain(createdTrain.id());
        assertThat(createdTrain).isEqualTo(retrievedTrain);
    }

    @Test
    void getTrain_ShouldRetrieveCorrectTrain() {
        Train newTrain = new Train(null, 20, Status.ACTIVE, 1500);
        Train addedTrain = trainService.createTrain(newTrain);
        Train retrievedTrain = trainService.getTrain(addedTrain.id());

        assertThat(retrievedTrain).isEqualTo(addedTrain);
    }

    @Test
    void getTrain_WithNonExistentId_ShouldReturnNull() {
        Train retrievedTrain = trainService.getTrain(UUID.randomUUID());

        assertThat(retrievedTrain).isNull();
    }

    @Test
    void createTrain_ExistingTrain_ShouldUpdateTrain() {
        Train newTrain = new Train(null, 20, Status.ACTIVE, 1500);
        Train createdTrain = trainService.createTrain(newTrain);

        Train updatedTrain = new Train(createdTrain.id(), 25, Status.MAINTENANCE, 2000);

        Train resultTrain = trainService.createTrain(updatedTrain);

        assertThat(resultTrain.id()).isEqualTo(createdTrain.id());
        assertThat(resultTrain.carriages()).isEqualTo(25);
        assertThat(resultTrain.status()).isEqualTo(Status.MAINTENANCE);
        assertThat(resultTrain.mileage()).isEqualTo(2000);
    }


    @Test
    void updateTrain_ExistingTrain_ShouldUpdateTrain() {
        Train newTrain = new Train(null, 20, Status.ACTIVE, 1500);
        Train createdTrain = trainService.createTrain(newTrain);

        Train updatedTrain = new Train(createdTrain.id(), 30, Status.DISABLED, 2000);
        Train updated = trainService.updateTrain(updatedTrain);

        assertThat(updated).isEqualTo(updatedTrain);

        Train retrievedTrain = trainService.getTrain(createdTrain.id());
        assertThat(retrievedTrain).isEqualTo(updatedTrain);
    }

    @Test
    void updateTrain_NonExistentTrain_ShouldThrowException() {
        Train updatedTrain = new Train(UUID.randomUUID(), 30, Status.DISABLED, 2000);

        assertThatThrownBy(() -> trainService.updateTrain(updatedTrain))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Train with ID = [" + updatedTrain.id() + "] not exists");
    }


    @Test
    void deleteTrain_ExistingTrain_ShouldDeleteTrain() {
        Train newTrain = new Train(null, 20, Status.ACTIVE, 1500);
        Train createdTrain = trainService.createTrain(newTrain);

        trainService.deleteTrain(createdTrain.id());

        Train retrievedTrain = trainService.getTrain(createdTrain.id());
        assertThat(retrievedTrain).isNull();
    }


    @Test
    void list_ShouldReturnListOfTrains() {
        var trainList = trainService.list();
        int initialSize = trainList.size();

        assertThat(trainList).isNotNull();
        assertThat(trainList).hasSize(10);

        Train newTrain = new Train(null, 20, Status.ACTIVE, 1500);
        trainService.createTrain(newTrain);

        var updatedTrainList = trainService.list();
        assertThat(updatedTrainList).hasSize(initialSize + 1);
    }


}