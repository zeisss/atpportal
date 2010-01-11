package org.tptp.model;

public class Algebra {
    private long id;
    private String name;
    private String comment;
    
    Algebra() {
        this("");
    }
    
    public Algebra(String name) {
        this(name, "");
    }
    
    public Algebra(String name, String comment) {
        this(-1, name, comment);
    }
    
    public Algebra(long id, String name, String comment) {
        this.id = id;
        this.name = name;
        this.comment = comment;
    }
    
    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public String getComment() { return this.comment; }
    
    public void setId(long l) { this.id = l; }
    public void setName(String s) { this.name = s; }
    public void setComment(String s) { this.comment = s; }
    
    public boolean equals(Object obj) {
        if(obj == null ) {
            return false;
        }
        if ( obj instanceof Algebra) {
            Algebra a = (Algebra) obj;
            return (id == a.id) &&
                   (name == null ? a.name == null : a.name.equals(name)) &&
                   (comment == null ? a.comment == null : a.comment.equals(comment));
            
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return ((int)this.id) + (name == null ? 0 : name.hashCode());
    }
    
    public String toString() {
        return "Algebra [id=" + id + ";name="+name+ ";comment=" + comment + "]";
    }
}