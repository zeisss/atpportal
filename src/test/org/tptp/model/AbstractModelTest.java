package org.tptp.model;

import java.io.*;
import java.util.*;
import org.junit.*;
import org.tptp.model.postgres.*;
import org.tptp.model.jdbc.*;

public abstract class AbstractModelTest
{
    private static SimplePooledConnectionFactory factory;
    
    @BeforeClass
    public static void setUpModel() {
        if ( factory == null ) {
            Properties prop = new Properties();
            try {
                prop.load(AbstractModelTest.class.getResourceAsStream("/test.properties"));
            } catch (IOException exc) {
                throw new IllegalStateException(exc);
            }
            factory = new SimplePooledConnectionFactory(prop);
            RepositoryFactory.setInstance(new PostgresRepositoryFactory(factory));
        }
        
    
    }
    
    @AfterClass
    public static void tearDownModel() {
        Assert.assertEquals(0, factory.getReturnedConnections().size());
        factory.shutdown();
        factory = null;
    }
}