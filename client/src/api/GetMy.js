import My from './My.json'

export default function GetMy() {
    return{
        id: My.my_id,
        name: My.my_name,
        field: My.my_field,
        equipment : My.my_equipment,
        job: My.my_job,
        introduction : My.my_introduction,
        area : My.my_area
    }         
}