/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import Automatas.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Fabian
 */
public class XMLReader {

    private XMLReader() {
    }

    public static XMLReader getInstance() {
        if (_Instance == null) {
            _Instance = new XMLReader();
        }
        return _Instance;
    }

    public DFA LeerXML(String pNombreArchivo) throws ParserConfigurationException, SAXException, IOException {
        File fXmlFile = new File(pNombreArchivo);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();


        List<String> estados = leerEtiquete(doc, "states", "state");
        List<String> alfabeto = leerEtiquete(doc, "alphabet", "char");
        String estado_inicial = leerInicial(doc, "initial-state");
        List<String> estados_finales = leerEtiquete(doc, "final-states", "state");
        List<Transicion> transiciones = leerTransiciones(doc);
  //NFA(List<String> pAlfabeto, List<Transicion> pTransiciones, List<String> pEstados, List<String> pEstados_Iniciales, List<String> pEstados_Finales)
        return new DFA(alfabeto,transiciones,estados,estado_inicial,estados_finales);
    }
    
        private String leerInicial(Document pDoc, String pEtiquete) {
        String Resultado ;

        NodeList nList = pDoc.getElementsByTagName(pEtiquete);
        Resultado = nList.item(0).getTextContent();



        return Resultado;
    }
    
    
    /**
     * DADO UN NOMBRE DE ETIQUETA,SACA LOS VALORES....PD:ESTE METODO PUEDE
     * FUNCIONAR PARA CUALQUIER TIPO DE XML
     *
     * @param pDoc
     * @param pEtiquete
     * @param pNombre
     * @return
     */
    private List<String> leerEtiquete(Document pDoc, String pEtiquete, String pNombre) {
        List<String> Resultado = new ArrayList<>();

        NodeList nList = pDoc.getElementsByTagName(pEtiquete);
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;

        for (int cont = 0; cont < eElement.getElementsByTagName(pNombre).getLength(); cont++) {
            Resultado.add(eElement.getElementsByTagName(pNombre).item(cont).getTextContent());
        }

        return Resultado;
    }

    /**
     * *
     * HACE UNA LECTURA DE LAS TRANSICIONES DEL LOS AUTOMATAS.PD: ESTE METODO
     * SOLO SIRVE PARA ESTE TRABAJO
     *
     * @param pDoc
     * @return
     */
    private List<Transicion> leerTransiciones(Document pDoc) {
        List<Transicion> Resultado = new ArrayList<>();
        Transicion temp;
        String source, destiny, valor;

        NodeList nList = pDoc.getElementsByTagName("transition");
        for (int cont = 0; cont < nList.getLength(); cont++) {

            Node nNode = nList.item(cont);
            Element eElement = (Element) nNode;


            source = eElement.getElementsByTagName("source").item(0).getTextContent();
            destiny = eElement.getElementsByTagName("destiny").item(0).getTextContent();
            valor = eElement.getElementsByTagName("char").item(0).getTextContent();
            //System.out.println(source+"-"+destiny+"-"+valor);
            Resultado.add(new Transicion(source, destiny, valor));
        }

        return Resultado;
    }
    private static XMLReader _Instance;
}
