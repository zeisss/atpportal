package org.tptp.model;

import java.io.*;
import java.util.*;
import org.junit.*;
import org.tptp.model.postgres.*;

public abstract class AbstractModelTest
{
    private static DefaultConnectionFactory factory;
    
    @BeforeClass
    public static void setUpModel() {
        if ( factory == null ) {
            Properties prop = new Properties();
            try {
                prop.load(AbstractModelTest.class.getResourceAsStream("/test.properties"));
            } catch (IOException exc) {
                throw new IllegalStateException(exc);
            }
            factory = new DefaultConnectionFactory(prop);
            RepositoryFactory.setInstance(new PostgresRepositoryFactory(factory));
        }
        
    
    }
    
    @AfterClass
    public static void tearDownModel() {
        factory.shutdown();
        factory = null;
    }
}