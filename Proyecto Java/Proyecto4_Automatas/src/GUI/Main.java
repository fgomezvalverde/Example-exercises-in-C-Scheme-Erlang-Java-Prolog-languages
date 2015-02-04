/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import Automatas.*;
import Utiles.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
/**
 *
 * @author Fabian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {
        //char[] pAlfabeto,List<Transicion> pTransiciones,List<String> pEstados,List<String> pEstados_Iniciales,List<String> pEstados_Finales
        
       /* List<String> alfabeto1 = Arrays.asList(
                "0",
                "1"
                ); 
        List<Transicion> transiciones1 = Arrays.asList(
                new Transicion("A","A","1"),
                new Transicion("A","B","0"),
                new Transicion("B","C","1"),
                new Transicion("B","B","0"),
                new Transicion("C","C","1"),
                new Transicion("C","C","0")
                );
        List<String> estados1 = Arrays.asList(
                "A",
                "B",
                "C"
                );
        String estado_Inicial1 ="A";
        List<String> estados_Finales1 = Arrays.asList(
                "A",
                "B"
                ); */
        
        
      List<String> alfabeto1 = Arrays.asList(
                "0",
                "1"
                ); 
        List<Transicion> transiciones1 = Arrays.asList(
                new Transicion("A","B","0"),
                new Transicion("B","B","0"),
                new Transicion("B","B","1")
                );
        List<String> estados1 = Arrays.asList(
                "A",
                "B");
        String estado_Inicial1 ="A";
        List<String> estados_Finales1 = Arrays.asList(
                "B"
                ); 
        //----------------------------
      List<String> alfabeto2 = Arrays.asList(
                "0",
                "1"
                ); 
        List<Transicion> transiciones2 = Arrays.asList(
                new Transicion("C","C","0"),
                new Transicion("C","D","1"),
                new Transicion("D","D","0"),
                new Transicion("D","D","1")
                );
        List<String> estados2 = Arrays.asList(
                "C",
                "D");
        String estado_Inicial2 ="C";
        List<String> estados_Finales2 = Arrays.asList(
                "D"
                );
        
        DFA automata1 = new DFA(alfabeto1,transiciones1,estados1,estado_Inicial1,estados_Finales1);
        DFA automata2 = new DFA(alfabeto2,transiciones2,estados2,estado_Inicial2,estados_Finales2);

        automata1.mix(automata2, 2);
        
        StringGenerator gene = new StringGenerator(alfabeto2);
        System.out.println(gene.getHilera(5,10));
        
        
        
        //XMLGenerator.getInstancia().CrearXML(automata1, "C:\\Users\\Fabian\\Desktop\\Progra\\lenguajes de programacion\\Proyecto Java\\Prueba XML\\file1.xml");
        //NFA a =XMLReader.getInstance().LeerXML("C:\\Users\\Fabian\\Desktop\\Progra\\lenguajes de programacion\\Proyecto Java\\Prueba XML\\file1.xml");
        
        //DFA b = automata1.mix(automata2,1);
        XMLGenerator.getInstancia().CrearXML((DFA) automata1, "C:\\Users\\Fabian\\Desktop\\Progra\\lenguajes de programacion\\Proyecto Java\\Prueba XML\\file4 NFA.xml");
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });

    }
}
