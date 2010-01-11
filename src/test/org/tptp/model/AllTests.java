package org.tptp.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  AccountTest.class,
  AlgebraTest.class,
  AtpTest.class,
  FormulaTest.class,
  ProofTest.class,
  QueueJobTest.class,
  TransactionTest.class
})
public class AllTests {
    
}