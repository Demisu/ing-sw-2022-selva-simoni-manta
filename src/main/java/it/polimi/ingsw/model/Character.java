package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;

import java.util.Random;

public class Character extends StudentAccessiblePiece {

    //General
    private Integer cost;
    private String image;

    //Setup info
    private Boolean setup_required;
    private String setup_object;
    //private Set<Integer> students; //Needs to be populated after reading the JSON
    private Integer setup_number;

    //Effect parameters
    private String effect_type;
    private String effect_scope;
    private String effect_object;
    private String effect_condition;
    private String effect_choose_condition;

    private String effect_source;
    private String effect_choose_source;
    private String effect_target;
    private String effect_choose_target;

    private Integer effect_number_min;
    private Integer effect_number_max;
    private String effect_choose_number;

    //Game
    private boolean hasBeenUsed;
    private boolean hasIncreasedCost;

    //Utility
    GameController gameController;

    public Character(){}

    //DO NOT DELETE, CONTAINS OLD BIG CONSTRUCTOR
    /*public Character(Integer cost, String image,                                                                    //General
                     Boolean setup_required, String setup_object, Integer setup_number,                                  //Setup
                     String effect_type, String effect_scope, String effect_object, String effect_condition,                //Effect
                     String effect_choose_condition,                                                                      //Effect
                     String effect_source, String effect_choose_source, String effect_target, String effect_choose_target,    //Effect source/target
                     Integer effect_number_min, Integer effect_number_max, String effect_choose_number) {                     //Effect number

        this.cost = cost;
        this.image = image;

        this.setup_required = setup_required;
        this.setup_object = setup_object;
        this.setup_number = setup_number;

        //Populates the character if needed
        if(setup_object.equals("student")){
            students = new HashSet<Integer>();
            for(int i = 0; i < setup_number; i++){
                students.add(GameController.getReferenceGame().getAStudent());
            }
        }

        this.effect_type = effect_type;
        this.effect_scope = effect_scope;
        this.effect_object = effect_object;
        this.effect_condition = effect_condition;
        this.effect_choose_condition = effect_choose_condition;
        this.effect_source = effect_source;
        this.effect_choose_source = effect_choose_source;
        this.effect_target = effect_target;
        this.effect_choose_target = effect_choose_target;

        this.effect_number_min = effect_number_min;
        this.effect_number_max = effect_number_max;
        this.effect_choose_number = effect_choose_number;
        
    }
    */

    public void effect(){

        switch (effect_type) {
            case "move" -> this.move();
            case "exchange" -> this.exchange();
            case "add" -> this.add();
            case "resolve" -> this.resolve();

            /*Extra cases...
            case "???":
                break;
            */
            default -> System.out.println("Cannot determine effect type");
        }

    }

    public void move(){

        //All the original characters of type exchange ask a range
        //If more characters are added, other checks may be needed

        switch(effect_choose_target){
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

        for(int i = 0; i < effect_number_max; i++){
            //Ask which student to select in (effect_source), or quit
            //if(quit) -> break;
            //else
            //Ask for target student to select in (effect_target)
            //Swap(source, target)
        }

    }

    public void add(){

        //Not implementing extra cases out of the original 12 Characters.
        //May need to add more if new characters are added.

        switch(effect_object) {
            case "influence":
                Game.setInfluenceModifier(Game.getInfluenceModifier() + effect_number_min); //(no range ATM)
                break;
            case "mother_nature_movement":
                Game.setMotherNatureMovements(Game.getMotherNatureMovements() + effect_number_min); //(no range ATM)
                break;
            case "student":
                //Student (color?) influence += effect_number_min
                switch(effect_condition){
                    case "color":
                        //Should need more selection, but this is enough with only
                        //The original characters
                        //Ask the player for a color to change
                        //Student COLOR influence += effect_number_min
                        break;
                    /*Extra cases...
                    case "???":
                        break;
                    */
                }
                break;
            case "tower":
                Game.setTowerValue(Game.getTowerValue() + effect_number_min); //(no range ATM)
                break;
            case "professor":
                //When counting DiningRoom students, consider count++
                Game.setStudentsInDiningModifier(effect_number_min);                break;
            /*Extra cases...
            case "???":
                break;
            */
        }

    }

    public void resolve(){

        Integer targetID = 0;

        if(effect_choose_target == "player"){
            //Ask the player for the ID (e.g.: ID of target island)
            //TO CHANGE
            targetID = new Random().nextInt(gameController.getCurrentGame().getIslands().size());
        } else if(effect_choose_target == "random"){
            //Get a random ID (e.g.: ID of target island)
            targetID = new Random().nextInt(gameController.getCurrentGame().getIslands().size());
        }

        //Call controller to resolve the target (ID) (?)
        gameController.resolveIsland(targetID);
    }

    public boolean getHasIncreasedCost(){
        return hasIncreasedCost;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public boolean getHasBeenUsed(){
        return hasBeenUsed;
    }

    public void setHasBeenUsed(boolean hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
