import {Select} from "@hilla/react-components/Select";
import {Button} from "@hilla/react-components/Button";
import {useForm} from "@hilla/react-form";
import {useEffect} from "react";
import {NumberField} from "@hilla/react-components/NumberField";
import Train from "Frontend/generated/io/js/domain/Train";
import TrainModel from "Frontend/generated/io/js/domain/TrainModel";
import Status from "Frontend/generated/io/js/domain/Status";

interface TrainFormProps {
    train?: Train | null;
    onSubmit?: (trainModel: Train) => Promise<void>;
}

export default function TrainsForm({train, onSubmit}: TrainFormProps) {
    const {field, model, submit, reset, read} = useForm(TrainModel, {onSubmit});

    const statusValues = Object.values(Status);
    const statusOptions = statusValues.map(status => ({label: status, value: status}));


    useEffect(() => {
        read(train);
    }, [train]);


    return (
        <div className="flex flex-col gap-s items-start">
            <NumberField label="Carriages" {...field(model.carriages)} />
            <Select label="Status" items={statusOptions} {...field(model.status)} />
            <NumberField label="Mileage" {...field(model.mileage)} />


            <div className="flex gap-m">
                <Button onClick={submit} theme="primary">Save</Button>
            </div>
        </div>
    )
}