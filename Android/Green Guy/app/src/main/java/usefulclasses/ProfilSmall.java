package usefulclasses;

import com.google.android.gms.maps.model.LatLng;
//! représente un profil partiel
/*!
  A remplir
*/
public class ProfilSmall {
    private long id;
    private String realName;
    private String pseudo;
    private String description;
    private String mail;
    private LatLng lastPosition=new LatLng(0,0);
    public ProfilSmall (String realName,String pseudo,String description,String mail,LatLng lastPosition,long id){
        this.realName=realName;
        this.pseudo=pseudo;
        this.description=description;
        this.mail=mail;
        this.lastPosition=lastPosition;
        this.id=id;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LatLng getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(LatLng lastPosition) {
        this.lastPosition = lastPosition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
