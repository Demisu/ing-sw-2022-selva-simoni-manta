import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestClass {

    @BeforeEach
    void testSetup() {
        StudentAccessiblePiece sap = new StudentAccessiblePiece();
    }
    
    //@RepeatedTest(5)
    @Test
    @DisplayName("Testing the appropriate return index of colors")
    void testGetAStudent() {
        assertEquals(0, sap.indexOfColor(Color.YELLOW), "Index 0 should correspond to yellow");
        assertEquals(1, sap.indexOfColor(Color.BLUE), "Index 1 should correspond to blue");
        assertEquals(2, sap.indexOfColor(Color.RED), "Index 2 should correspond to red");
        assertEquals(3, sap.indexOfColor(Color.GREEN), "Index 3 should correspond to green");
        assertEquals(4, sap.indexOfColor(Color.PURPLE), "Index 4 should correspond to purple");
    }
    

}