package classes;

import net.jini.space.JavaSpace;

public class Generals {
	public JavaSpace space;
	
	
	public Generals() {
		loadSpace();
	}
	
	
	private void loadSpace() {
		Lookup finder = new Lookup(JavaSpace.class);
        space = (JavaSpace) finder.getService();
        
        if (space == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        } 
        
        System.out.println("O servico JavaSpace foi encontrado.");
	}

}
