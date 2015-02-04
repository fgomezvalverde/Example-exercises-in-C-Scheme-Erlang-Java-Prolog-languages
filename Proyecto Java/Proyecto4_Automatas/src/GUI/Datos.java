/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import Automatas.*;
import Gramatica.*;
import Utiles.*;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Fabian
 */
public class Datos {
    private Datos(){}
    
    public static Datos getInstance()
    {
        if(Instance==null)
        {
            Instance = new Datos();
        }
        return Instance;
    }
    
    public DFA getAutomata1()
    {
        return automata1;
    }
    
    public DFA getAutomata2()
    {
        return automata2;
    }
    
    
    public StringGenerator getGenerador()
    {
        return generador;
    }
    
    public Grammar getGramatica()
    {
        return gramatica;
    }

    public void setAutomata1(String automata1) throws ParserConfigurationException, SAXException, IOException {
        Datos.automata1 = XMLReader.getInstance().LeerXML(automata1);
    }

    public void setAutomata2(String automata2) throws ParserConfigurationException, SAXException, IOException {
        Datos.automata2 =  XMLReader.getInstance().LeerXML(automata2);
    }

    
    public void setAutomata1TODO(DFA nuevo) throws ParserConfigurationException, SAXException, IOException {
        Datos.automata1 = nuevo;
    }

    public void setAutomata2TODO(DFA nuevo) throws ParserConfigurationException, SAXException, IOException {
        Datos.automata2 =  nuevo;
    }
    
    public void setGenerador(List<String> generador) {
        Datos.generador = new StringGenerator(generador);
    }

    public void setGramatica(Grammar gramatica) {
        Datos.gramatica = gramatica;
    }

    
    
    private static DFA automata1;
    private static DFA automata2;
    private static StringGenerator generador;
    private static Grammar gramatica;
    
    private static Datos Instance;
}
