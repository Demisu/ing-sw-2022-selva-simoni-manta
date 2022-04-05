import java.util.HashSet;

public class Character extends StudentAccessiblePiece{

    //General
    private Integer cost;
    private String imagePath;

    //Setup info
    private Boolean setupRequired;
    private String contentType;
    private Set<Integer> students; //Needs to be populated after reading the JSON
    private Integer contentNumber;

    //Effect parameters
    private String effectType;
    private String effectScope;
    private String effectObject;
    private String effectCondition;
    private String effectChooseCondition;

    private String effectSource;
    private String effectChooseSource;
    private String effectTarget;
    private String effectChooseTarget;

    private Integer effectNumberMin;
    private Integer effectNumberMax;
    private String effectChooseNumber;

    //Game
    private boolean hasBeenUsed;

    public Character(){}

    public Character(Integer cost, String imagePath,                                                                    //General
                     Boolean setupRequired, String contentType, Integer contentNumber,                                  //Setup
                     String effectType, String effectScope, String effectObject, String effectCondition,                //Effect 
                     String effectChooseCondition,                                                                      //Effect 
                     String effectSource, String effectChooseSource, String effectTarget, String effectChooseTarget,    //Effect source/target
                     Integer effectNumberMin, Integer effectNumberMax, String effectChooseNumber) {                     //Effect number

        this.cost = cost;
        this.imagePath = imagePath;

        this.setupRequired = setupRequired;
        this.contentType = contentType;
        this.contentNumber = contentNumber;

        //Populates the character if needed
        if(contentType == "student"){
            students = new HashSet<Integer>();
            for(int i = 0; i < contentNumber; i++){
                students.add(game.getAStudent());
            }
        }

        this.effectType = effectType;
        this.effectScope = effectScope;
        this.effectObject = effectObject;
        this.effectCondition = effectCondition;
        this.effectChooseCondition = effectChooseCondition;
        this.effectSource = effectSource;
        this.effectChooseSource = effectChooseSource;
        this.effectTarget = effectTarget;
        this.effectChooseTarget = effectChooseTarget;

        this.effectNumberMin = effectNumberMin;
        this.effectNumberMax = effectNumberMax;
        this.effectChooseNumber = effectChooseNumber;
        
    }

    public void effect(){

        switch(effectType){
            case "move":
                this.move();
                break;
            case "exchange":
                this.exchange();
                break;
            case "add":
                this.add();
                break;
            case "resolve":
                this.resolve();
                break;
            /*Extra cases...
            case "???":
                break;
            */
            default:
                system.out.println("Cannot determine effect type");
                break;
        }     

    }

    public void move(){

        //All the original characters of type exchange ask a range
        //If more characters are added, other checks may be needed

        switch(effectChooseTarget){
            case "player":
                //ask player 
                break;
            case "no":
                //...
                break;
            /*Extra cases...
            case "???":
                break;
            */
        }
        //gamecontroller.move(source, target)

    }

    public void exchange(){

        //Both the original characters of type exchange ask a range
        //If more characters are added, other checks may be needed

        for(int i = 0; i < effectNumberMax; i++){
            //Ask which student to select in (effectSource), or quit
            //if(quit) -> break;
            //else
            //Ask for target student to select in (effectTarget)
            //Swap(source, target)
        }

    }

    public void add(){

        //Not implementing extra cases out of the original 12 Characters.
        //May need to add more if new characters are added.

        switch(effectObject) {
            case "influence":
                //influence += effectNumberMin; //(no range ATM)
                break;
            case "mother_nature_movement":
                //motherNatureMovement += effectNumberMin; //(no range ATM)
                break;
            case "student":
                //Student (color?) influence += effectNumberMin
                switch(effectCondition){
                    case "color":
                        //Should need more selection, but this is enough with only
                        //The original characters
                        //Ask the player for a color to change
                        //Student COLOR influence += effectNumberMin
                        break;
                    /*Extra cases...
                    case "???":
                        break;
                    */
                }
                break;
            case "tower":
                //Tower influence += effectNumberMin; //(no range ATM)
                break;
            case "professor":
                //When counting DiningRoom students, consider count++
                break;
            /*Extra cases...
            case "???":
                break;
            */
        }

    }

    public void resolve(){

        if(effectChooseTarget == "player"){
            //Ask the player for the ID (e.g.: ID of target island)
        } else if(effectChooseTarget == "random"){
            //Get a random ID (e.g.: ID of target island)
        }

        //Call controller to resolve the target (ID) (?)
    }

}
