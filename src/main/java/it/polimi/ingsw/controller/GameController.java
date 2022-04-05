public class GameController {

    public void moveStudent(Integer student, StudentAccessiblePiece origin, StudentAccessiblePiece target){
        origin.removeStudent(student);
        target.addStudent(student);
        return;
    }

    public void moveProfessor(int professorColor, SchoolBoard origin, SchoolBoard target){
        origin.setProfessor(professorColor, false);
        target.setProfessor(professorColor, true);
        return;
    }

    public void getStudent(){

    }
    
}
