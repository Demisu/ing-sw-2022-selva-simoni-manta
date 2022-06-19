package it.polimi.ingsw.model;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.controller.GameController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Class for the Character cards
 */
public class Character extends StudentAccessiblePiece implements Serializable {

    //General
    private Integer cost;
    private String image;
    private Integer noEntryNumber;

    //Setup info
    private Boolean setup_required;
    private String setup_object;
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
    private transient GameController gameController;

    /**
     *
     * Custom builder for the view.
     * Omits Variables not useful for user interaction.
     *
     * @param cost cost of the character
     * @param image image to show
     * @param hasIncreasedCost flag to display a coin on top of the character
     * @param students students contained, may be empty
     * @param noEntryNumber no entry pieces contained, may be 0
     */
    public Character(Integer cost, String image, Boolean hasIncreasedCost, HashSet<Integer> students, Integer noEntryNumber){

        this.cost = cost;
        this.image = image;
        this.hasIncreasedCost = hasIncreasedCost;
        this.students = students;
        this.noEntryNumber = noEntryNumber;

    }

    public Character(){
        super();
    }

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

    /**
     * Depending on the character type,
     * calls the relative effect (move/exchange/add/resolve)
     */
    public void effect(PlayCharacterRequest req){

        switch (effect_type) {
            case "move" -> this.move(req);
            case "exchange" -> this.exchange(req);
            case "add" -> this.add(req);
            case "resolve" -> this.resolve(req);
            default -> System.out.println("Cannot determine effect type");
        }

    }

    /**
     * Used by move-type characters, performs the action
     */
    public void move(PlayCharacterRequest req){

        List<Integer> studentsToMove = req.getStudentsInOrigin();
        List<Integer> originPieces = req.getOriginPieces();
        List<Integer> targetPieces = req.getTargetPieces();

        //If source is null, this character is the source for every movement
        if(originPieces == null || originPieces.isEmpty()){
            originPieces = new ArrayList<>();
            for(Integer index : targetPieces){
                originPieces.add(this.getPieceID());
            }
        }

        //All the original characters of type move ask a range
        //If more characters are added, other checks may be needed

        switch(effect_choose_target){
            case "player":
                for(int i = 0; i < studentsToMove.size(); i++){
                    gameController.moveStudent(studentsToMove.get(i), originPieces.get(i), targetPieces.get(i));
                }
                break;
            case "no":
                //...
                break;
            /*Extra cases...
            case "???":
                break;
            */
        }

    }

    /**
     * Used by exchange-type characters, performs the action
     */
    public void exchange(PlayCharacterRequest req){

        List<Integer> studentsInOrigin = req.getStudentsInOrigin();
        List<Integer> studentsInTarget = req.getStudentsInTarget();
        List<Integer> exchangeOriginList = req.getOriginPieces();
        List<Integer> exchangeTargetList = req.getTargetPieces();

        //Both the original characters of type exchange ask a range
        //If more characters are added, other checks may be needed
        Integer studentOrigin, studentTarget, pieceOrigin, pieceTarget;

        for(int i = 0; i < effect_number_max; i++){
            studentOrigin = studentsInOrigin.get(i);
            studentTarget = studentsInTarget.get(i);
            pieceOrigin = exchangeOriginList.get(i);
            pieceTarget = exchangeTargetList.get(i);
            //Origin to target
            gameController.moveStudent(studentOrigin, pieceOrigin, pieceTarget);
            //Target to origin
            gameController.moveStudent(studentTarget, pieceTarget, pieceOrigin);
        }
    }

    /**
     * Used by add-type characters, adds a certain value to the game modifiers
     */
    public void add(PlayCharacterRequest req){

        //Not implementing extra cases out of the original 12 Characters.
        //May need to add more if new characters are added.

        switch(effect_object) {
            case "influence":
                Game.setInfluenceModifier(Game.getInfluenceModifier() + effect_number_min); //(no range ATM)
                break;
            case "mother_nature":
                Game.setMotherNatureMovements(Game.getMotherNatureMovements() + effect_number_min); //(no range ATM)
                break;
            case "student":
                //Student (color?) influence += effect_number_min
                switch(effect_condition){
                    case "color":
                        Color targetColor = req.getTargetColor();
                        //Should need more selection, but this is enough with only The original characters
                        Integer colorID = StudentAccessiblePiece.indexOfColor(targetColor);
                        Game.setStudentValue(targetColor, Game.getStudentValue(colorID) + 1);
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
            case "student_dining":
                //When counting DiningRoom students, consider count++
                Game.setStudentsInDiningModifier(effect_number_min);
                break;
            case "no_entry":
                for(Integer target : req.getTargetPieces()){
                    Island targetIsland = gameController.getCurrentGame().getIslandByID(target);
                    if(!(targetIsland == null)){
                        targetIsland.setNoEntry(targetIsland.getNoEntry() + 1);
                    }
                }
                break;
            /*Extra cases...
            case "???":
                break;
            */
        }

    }

    /**
     * Used by resolve-type characters, resolves the target island
     */
    public void resolve(PlayCharacterRequest req){

        List<Integer> targetPieces = req.getTargetPieces();
        if(effect_choose_target.equals("player")){
            for(Integer targetID : targetPieces){
                gameController.resolveIsland(targetID);
            }
        }
        /* Extra case, for example:
        else if(effect_choose_target.equals("random")){
            //Get a random ID (e.g.: ID of target island)
            Integer targetID = new Random().nextInt(gameController.getCurrentGame().getIslands().size());
            gameController.resolveIsland(targetID);
        }
        */
    }

    /**
     * @return boolean flag, true if the character's cost has been increased
     */
    public boolean getHasIncreasedCost(){
        return hasIncreasedCost;
    }

    /**
     * @return cost of the character
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * @return image of the character
     */
    public String getImage() {
        return image;
    }

    /**
     * @return no entry pieces contained, may be 0
     */
    public Integer getNoEntryNumber() {
        return noEntryNumber;
    }

    /**
     * @return boolean flag, true if character has been used during current turn
     */
    public boolean getHasBeenUsed(){
        return hasBeenUsed;
    }

    /**
     * @param cost the character's cost
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * @param hasBeenUsed boolean flag, should be true if character has been used during current turn
     */
    public void setHasBeenUsed(boolean hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * @param gameController gameController for certain effects
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public String getSetupObject() {
        return setup_object;
    }

    public Integer getSetupNumber() {
        return setup_number;
    }

    public void setNoEntryNumber(Integer noEntryNumber) {
        this.noEntryNumber = noEntryNumber;
    }

    public String getEffectType() {
        return effect_type;
    }

    public String getEffectSource() {
        return effect_source;
    }

    public String getEffectTarget() {
        return effect_target;
    }

    public String getEffectChooseCondition() {
        return effect_choose_condition;
    }

    public String getEffectCondition() {
        return effect_condition;
    }

    public String getEffectObject() {
        return effect_object;
    }

    public Integer getEffectNumberMax() {
        return effect_number_max;
    }
}
