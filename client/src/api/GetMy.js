import My from './My.json'

export default function GetMy() { //굳이 이렇게 안 해도 되지만..
    const MyList = My.map(({ my_id, my_name, my_field,my_equipment, my_job, my_introduction, my_area }) => ({
        id: my_id,
        name: my_name,
        field: my_field,
        equipment : my_equipment,
        job:my_job,
        introduction : my_introduction,
        area : my_area
    }));
    
    return MyList;
}