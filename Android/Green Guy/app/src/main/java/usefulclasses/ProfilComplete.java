package usefulclasses;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ProfilComplete extends ProfilSmall {
    ArrayList<Long> friends = new ArrayList<Long>();
    // a ajouter : profil pic 

    public ProfilComplete (String realName, String pseudo, String description, String mail, LatLng lastPosition,long id){
        super(realName,pseudo,description,mail,lastPosition,id);
    }

    public void addFriend(ProfilSmall friend){
        friends.add(friend.getId());
    }
    public boolean isFriend(ProfilSmall person){
        return friends.contains(person.getId());
    }
}
