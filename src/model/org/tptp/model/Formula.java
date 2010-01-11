package org.tptp.model;

import java.util.*;

/**
 * NOTE: References do not hold a list of Formula's so the object tree is a top-down-tree without backreferences,
 *       which makes loading & storing easier. 
 */
public class Formula {
    private long id;
    private String name;
    private String formulaText;
    private String comment;
    private List<FormulaReference> references;
    
    private long algebraId;
    
    public Formula() {
        this("");
    }
    public Formula (String name) {
        this(name, "");
    }
    public Formula(String name, String formula) {
        this(name, formula, "");
    }
    public Formula (String name, String formula, String comment)
    {
        this(-1, name, formula, comment);
    }
    Formula ( long id, String name, String formula, String comment)
    {
        this.id = id;
        this.name = name;
        this.formulaText = formula;
        this.comment = comment;
        this.references = new LinkedList<FormulaReference>();
    }
    
    public void addReference(FormulaReference r) {
        if ( !this.references.contains(r)) {
            this.references.add(r);
        }
    }
    
    public void removeReference(FormulaReference r) {
        if (this.references.contains(r)) {
            this.references.remove(r);
        }
    }
    
    public void clearReferences() {
        this.references.clear();
    }
    
    public List<FormulaReference> getReferences() { return Collections.unmodifiableList(this.references); }
    
    public String toString() {
        return "Formula [id=" + id + ";name="+ name + "]";
    }
    
    public boolean equals(Object obj) {
        if ( obj instanceof Formula ) {
            Formula f2 = (Formula) obj;
            
            if ( (f2.id == id) &&
                   (name == null ? f2.name == null : (name.equals(f2.name))) &&
                   (comment == null ? f2.comment == null : (comment.equals(f2.comment))) &&
                   (formulaText == null ? f2.formulaText == null : (formulaText.equals(f2.formulaText)))
               ) {
                
                for ( FormulaReference ref : references ) {
                    if ( !f2.references.contains(ref)) {
                        return false;
                    }
                }
                
                return true;
            }
        }
        
        return false;
        
    }
    public int hashCode() {
        return (int)id;
    }
    
    
    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public String getFormulaText() { return this.formulaText; }
    public String getComment() { return this.comment; }
    
    public void setId(long l) { this.id = l; }
    public void setName(String s) { this.name = s; }
    public void setFormulaText(String s) { this.formulaText = s; }
    public void setComment(String s) { this.comment = s; }
}