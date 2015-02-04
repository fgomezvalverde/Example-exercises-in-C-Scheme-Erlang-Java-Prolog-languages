/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import Automatas.*;
import java.util.List;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Fabian
 */
public class XMLGenerator {

    /**
     * *
     * SE CREA LA ETIQUETA PRINCIPAL DEL XML
     *
     * @param pNombreRoot
     * @throws ParserConfigurationException
     */
    private XMLGenerator() throws ParserConfigurationException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        doc = docBuilder.newDocument();
        rootElement = doc.createElement(nombre_Padre_Root);
        doc.appendChild(rootElement);
    }

    public static XMLGenerator getInstancia() throws ParserConfigurationException
    {
        if(Instancia == null)
        {
            Instancia = new XMLGenerator();
        }
        return Instancia;
    }
    
    public void CrearXML(DFA pAutomata, String pDireccion) throws TransformerConfigurationException, TransformerException {
        PonerEtiquete(pAutomata.getEstados(),"states","state");
        PonerEtiquete(pAutomata.getAlphabet(),"alphabet","char");
        
        PonerTransiciones(pAutomata.getTransiciones());
        
        PonerInicial(pAutomata.getEstado_Inicial(),"initial-state");
        PonerEtiquete(pAutomata.getEstados_Finales(),"final-states","state");
        
        
        
        // SE CREA EL FILE DEL XML Y SE GUARDA EN LA DIRECCION
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(pDireccion));
        transformer.transform(source, result);

        
        System.out.println("SE GUARDO");
    }

    /**
     * *
     * SE PONEN LOS ESTADOS DEL AUTOMATA EN EL XML
     *
     * @param pEstados
     */
    private void PonerEtiquete(List<String> pEstados,String pEtiquete,String pNombre_Valor) {
        Element EtiquetePadre = doc.createElement(pEtiquete);
        rootElement.appendChild(EtiquetePadre);

        Element estadoHijo;

        for (int contador = 0; contador < pEstados.size(); contador++) {
            estadoHijo = doc.createElement(pNombre_Valor);
            estadoHijo.appendChild(doc.createTextNode(pEstados.get(contador)));
            EtiquetePadre.appendChild(estadoHijo);
        }
    }
    
    private void PonerTransiciones(List<Transicion> pTransiciones) {
        Element EtiquetePadre = doc.createElement("transitions");
        rootElement.appendChild(EtiquetePadre);

        Element EtiqueteHijo;
        Element estadoHijo;
        for (int contador = 0; contador < pTransiciones.size(); contador++) {
            EtiqueteHijo = doc.createElement("transition");
            EtiquetePadre.appendChild(EtiqueteHijo);
            
            
            estadoHijo = doc.createElement("source");
            estadoHijo.appendChild(doc.createTextNode(((String)pTransiciones.get(contador).getInicio())));
            EtiqueteHijo.appendChild(estadoHijo);
            
            estadoHijo = doc.createElement("char");
            estadoHijo.appendChild(doc.createTextNode(pTransiciones.get(contador).getValor()));
            EtiqueteHijo.appendChild(estadoHijo);
            
            estadoHijo = doc.createElement("destiny");
            estadoHijo.appendChild(doc.createTextNode(((String)pTransiciones.get(contador).getFinal())));
            EtiqueteHijo.appendChild(estadoHijo);
        }
    }
    
    private void PonerInicial(String pEstado,String pEtiquete)
    {
        Element EtiquetePadre = doc.createElement(pEtiquete);
        EtiquetePadre.appendChild(doc.createTextNode(pEstado));
        rootElement.appendChild(EtiquetePadre);
    }
    
    private Document doc;
    private Element rootElement;
    private static XMLGenerator Instancia;
    private final String nombre_Padre_Root = "automaton";
}
