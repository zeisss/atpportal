package org.tptp.model;

import java.util.*;

public class FormulaReference {
    //private long id;
    private String abbreviation;
    private String authors;
    private String title;
    private int year;
    
    
    FormulaReference() {
        this(-1, null, null, null, 0);
    }
    
    public FormulaReference(String abbr, String authors, String title, int year ) {
        this(-1, abbr, authors, title, year);
    }
    
    private FormulaReference(long id, String abbr, String authors, String title, int year)
    {
        //this.id = id;
        this.abbreviation = abbr;
        this.authors = authors;
        this.title = title;
        this.year = year;
    }
    
    public String toString() {
        return "FormulaReference [abbr=" + abbreviation + ";authors=" + authors + ";title=" + title + ";year=" + year + "]";
    }
    
    public boolean equals(Object obj) {
        if ( obj instanceof FormulaReference ) {
            FormulaReference ref = (FormulaReference) obj;
            return //this.id == ref.id &&
                abbreviation == null ? ref.abbreviation == null : (abbreviation.equals(ref.abbreviation)) &&
                authors == null ? ref.authors == null : (authors.equals(ref.authors)) &&
                title == null ? ref.title == null : (title.equals(ref.title)) &&
                year == ref.year;
        }
        return false;
    }
    
    public int hashCode() {
        return abbreviation.hashCode();
    }
    
    //public long getId() { return this.id; }
    public String getAbbreviation() { return this.abbreviation; }
    public String getAuthors() { return this.authors; }
    public String getTitle() { return this.title; }
    public int getYear() { return this.year; }
    
    //public void setId(long l) { this.id = l; }
    public void setAbbreviation(String s) { this.abbreviation = s; }
    public void setAuthors(String s) { this.authors = s; }
    public void setTitle(String s) { this.title = s; }
    public void setYear(int y) { this.year = y; }
}