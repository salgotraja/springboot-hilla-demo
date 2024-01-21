import {useEffect, useState} from 'react'
import {TrainService} from "Frontend/generated/endpoints";
import {Grid} from "@hilla/react-components/Grid";
import {GridColumn} from "@hilla/react-components/GridColumn";
import Train from 'Frontend/generated/io/js/domain/Train';
import TrainsForm from './TrainFormProps';

export default function TrainsView() {
    const [trains, setTrains] = useState<Train[]>([]);
    const [selected, setSelected] = useState<Train | null | undefined>();

    useEffect(() => {
        TrainService.list().then((trainsList) => {
            const validTrains = trainsList?.filter((train): train is Train => !!train) || [];
            setTrains(validTrains);
        });
    }, []);

    /*async function onTrainSaved(train: Train) {
        const saved = await TrainService.createTrain(train);
        if (saved && train.id) {
            setTrains(trains => trains.map(current => current.id === saved.id ? saved : current));
        } else if (saved) {
            setTrains(trains => [...trains, saved]);
        }
        setSelected(saved || undefined);
    }*/

    async function onTrainSaved(train: Train) {
        let saved: Train | undefined | null = null;
        if (train.id) {
            // Train has an ID, so update it
            saved = await TrainService.updateTrain(train).catch(() => null);
        } else {
            // Train doesn't have an ID, so create it
            saved = await TrainService.createTrain(train).catch(() => null);
        }

        if (saved) {
            setTrains(trains => {
                // Update or add the train and filter out any null values
                const updatedTrains = trains.map(t => t.id === saved!.id ? saved! : t);
                const newTrainList = train.id ? updatedTrains : [...trains, saved!];
                return newTrainList.filter(t => t !== null);
            });
            setSelected(saved);
        } else {
            // Handle error case
            // Depending on your application, you might want to show an error message here
        }
    }



    return (
        <div className="p-m flex gap-m">
            <Grid
                items={trains}
                onActiveItemChanged={e => setSelected(e.detail.value)}
                selectedItems={[selected]}>

                <GridColumn path="id"/>
                <GridColumn path="carriages"/>
                <GridColumn path="status"/>
                <GridColumn path="mileage"/>
            </Grid>
            {selected &&
                <TrainsForm train={selected} onSubmit={onTrainSaved}/>
            }

        </div>
    );
}