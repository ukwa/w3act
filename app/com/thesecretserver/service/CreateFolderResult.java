
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CreateFolderResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreateFolderResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="FolderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateFolderResult", propOrder = {
    "folderId"
})
public class CreateFolderResult
    extends WebServiceResult
{

    @XmlElement(name = "FolderId")
    protected int folderId;

    /**
     * Ruft den Wert der folderId-Eigenschaft ab.
     * 
     */
    public int getFolderId() {
        return folderId;
    }

    /**
     * Legt den Wert der folderId-Eigenschaft fest.
     * 
     */
    public void setFolderId(int value) {
        this.folderId = value;
    }

}
