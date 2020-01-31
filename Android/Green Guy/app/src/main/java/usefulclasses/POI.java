package usefulclasses;
//!  représente un Point d'intérêt
/*!
  A remplir
*/
public class POI {
    private String description ="Veuillez ajouter une dscription";
    private String title;
    public POI (String title,String des){
        this.description=des;
        this.title=title;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
