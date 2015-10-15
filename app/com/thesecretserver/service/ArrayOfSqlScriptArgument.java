
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfSqlScriptArgument complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSqlScriptArgument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SqlScriptArgument" type="{urn:thesecretserver.com}SqlScriptArgument" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSqlScriptArgument", propOrder = {
    "sqlScriptArgument"
})
public class ArrayOfSqlScriptArgument {

    @XmlElement(name = "SqlScriptArgument", nillable = true)
    protected List<SqlScriptArgument> sqlScriptArgument;

    /**
     * Gets the value of the sqlScriptArgument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sqlScriptArgument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSqlScriptArgument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SqlScriptArgument }
     * 
     * 
     */
    public List<SqlScriptArgument> getSqlScriptArgument() {
        if (sqlScriptArgument == null) {
            sqlScriptArgument = new ArrayList<SqlScriptArgument>();
        }
        return this.sqlScriptArgument;
    }

}
