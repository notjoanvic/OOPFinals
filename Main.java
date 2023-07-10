package shophomepage;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
    
    public static void main(String[] args) {
        
        Homepage hp = new Homepage();
        
        hp.setTitle("ITBoutique");
        hp.setSize(500, 400);
        hp.setDefaultCloseOperation(EXIT_ON_CLOSE);
        hp.setVisible(true);
    }
    
}
