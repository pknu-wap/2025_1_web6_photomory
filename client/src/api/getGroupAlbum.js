import groupAlbum from './groupAlbum.json';

export default function getGroupAlbum(groupId){
    if(!groupId) return [];
    const group =groupAlbum.find((g)=>g.id===groupId);
    return group? group.albums : [];
}