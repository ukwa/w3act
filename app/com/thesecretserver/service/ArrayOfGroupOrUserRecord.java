
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfGroupOrUserRecord complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfGroupOrUserRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GroupOrUserRecord" type="{urn:thesecretserver.com}GroupOrUserRecord" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfGroupOrUserRecord", propOrder = {
    "groupOrUserRecord"
})
public class ArrayOfGroupOrUserRecord {

    @XmlElement(name = "GroupOrUserRecord", nillable = true)
    protected List<GroupOrUserRecord> groupOrUserRecord;

    /**
     * Gets the value of the groupOrUserRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groupOrUserRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroupOrUserRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GroupOrUserRecord }
     * 
     * 
     */
    public List<GroupOrUserRecord> getGroupOrUserRecord() {
        if (groupOrUserRecord == null) {
            groupOrUserRecord = new ArrayList<GroupOrUserRecord>();
        }
        return this.groupOrUserRecord;
    }

}
