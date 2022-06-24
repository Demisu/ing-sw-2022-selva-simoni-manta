package it.polimi.ingsw.model;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.controller.GameController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Class for the Character cards
 */
public class Character extends StudentAccessiblePiece implements Serializable {

    //General
    private Integer cost;
    private String image;
    private Integer noEntryNumber;
    private String description;

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

    /**
     * Constructor used by JSON parsing of the character
     */
    public Character(){
        super();
    }

    /**
     * Depending on the character type,
     * calls the relative effect (move/exchange/add/resolve)
     *
     * @param req PlayCharacterRequest
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
     *
     * @param req PlayCharacterRequest
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
     *
     * @param req PlayCharacterRequest
     */
    public void exchange(PlayCharacterRequest req){

        List<Integer> studentsInOrigin = req.getStudentsInOrigin();
        List<Integer> studentsInTarget = req.getStudentsInTarget();
        List<Integer> exchangeOriginList = req.getOriginPieces();
        List<Integer> exchangeTargetList = req.getTargetPieces();

        System.out.println(studentsInOrigin);
        System.out.println(studentsInTarget);
        System.out.println(exchangeOriginList);
        System.out.println(exchangeTargetList);

        //Both the original characters of type exchange ask a range
        //If more characters are added, other checks may be needed
        Integer studentOrigin, studentTarget, pieceOrigin, pieceTarget;

        for(int i = 0; i < studentsInOrigin.size(); i++){
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
     *
     * @param req PlayCharacterRequest
     */
    public void add(PlayCharacterRequest req){

        Game game = gameController.getCurrentGame();

        //Not implementing extra cases out of the original 12 Characters.
        //May need to add more if new characters are added.

        switch(effect_object) {
            case "influence":
                game.setInfluenceModifier(game.getInfluenceModifier() + effect_number_min); //(no range ATM)
                break;
            case "mother_nature":
                game.setMotherNatureMovements(game.getMotherNatureMovementsModifier() + effect_number_min); //(no range ATM)
                break;
            case "student":
                //Student (color?) influence += effect_number_min
                switch(effect_condition){
                    case "color":
                        Color targetColor = req.getTargetColor();
                        //Should need more selection, but this is enough with only The original characters
                        Integer colorID = StudentAccessiblePiece.indexOfColor(targetColor);
                        game.setStudentValue(targetColor, game.getStudentValue(colorID) + 1);
                        break;
                    /*Extra cases...
                    case "???":
                        break;
                    */
                }
                break;
            case "tower":
                game.setTowerValue(game.getTowerValue() + effect_number_min); //(no range ATM)
                break;
            case "student_dining":
                //When counting DiningRoom students, consider count++
                game.setStudentsInDiningModifier(effect_number_min);
                break;
            case "no_entry":
                for(Integer target : req.getTargetPieces()){
                    Island targetIsland = gameController.getCurrentGame().getIslandByID(target);
                    if(!(targetIsland == null) && noEntryNumber > 0){
                        targetIsland.setNoEntry(targetIsland.getNoEntry() + 1);
                        noEntryNumber--;
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
     *
     * @param req PlayCharacterRequest
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

    /**
     * @return setup_object
     */
    public String getSetupObject() {
        return setup_object;
    }

    /**
     * @return setup_number
     */
    public Integer getSetupNumber() {
        return setup_number;
    }

    /**
     * @param noEntryNumber noEntryNumber to be set (used when moving noEntry tiles)
     */
    public void setNoEntryNumber(Integer noEntryNumber) {
        this.noEntryNumber = noEntryNumber;
    }

    /**
     * @return effect_type
     */
    public String getEffectType() {
        return effect_type;
    }

    /**
     * @return effect_source
     */
    public String getEffectSource() {
        return effect_source;
    }

    /**
     * @return effect_target
     */
    public String getEffectTarget() {
        return effect_target;
    }

    /**
     * @return effect_condition
     */
    public String getEffectCondition() {
        return effect_condition;
    }

    /**
     * @return effect_object
     */
    public String getEffectObject() {
        return effect_object;
    }

    /**
     * @return effect_number_max
     */
    public Integer getEffectNumberMax() {
        return effect_number_max;
    }

    /**
     * @return description of character
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param effectType effect to set
     */
    public void setEffectType(String effectType) {
        this.effect_type = effectType;
    }
}
