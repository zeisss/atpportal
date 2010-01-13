package org.tptp.model;

import java.util.*;

/**
 * 
 */
public class Operator {
    private long id;
    private String name;
    private int binding;
    private String notation;
    private String symbol;
    private int arity;
    private String description;
    private String comment;
    private String symbol_intern;
    
    public Operator (){};
    
    public Operator ( long id, String name, int binding, String notation, String symbol, String intern, int arity, String description, String comment)
    {
        this.id = id;
        this.name = name;
        this.binding = binding;
        this.notation = notation;
        this.symbol = symbol;
        this.symbol_intern = intern;
        this.arity = arity;
        this.description = description;
        this.comment = comment;
    }
    
    public String toString() {
        return "Operator [id=" + id + ";name="+ name + ";binding="+ binding + ";notation="+ notation + ";symbol="+ symbol + ";symbol_intern=" + symbol_intern +";arity="+ arity + ";description="+ description + ";comment="+ comment + "]";
    }
    
    
    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public int getBinding() { return this.binding; }
    public String getNotation() { return this.notation; }
    public String getSymbol() { return this.symbol; }
    public String getSymbolIntern() { return this.symbol_intern; }
    public int getArity() { return this.arity; }
    public String getDescription() { return this.description; }
    public String getComment() { return this.comment; }
    
    public void setId(long l) { this.id = l; }
    public void setName(String s) { this.name = s; }
    public void setBinding(int i) { this.binding = i; }
    public void setNotation(String s) { this.notation = s; }
    public void setSymbol(String s) { this.symbol = s; }
    public void setSymbolIntern(String s) { this.symbol_intern = s; }
    public void setArity(int i) { this.arity = i; }
    public void setDescription(String s) { this.description = s; }
    public void setComment(String s) { this.comment = s; }
}