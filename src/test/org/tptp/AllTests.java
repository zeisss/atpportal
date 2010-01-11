package org.tptp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  org.tptp.model.AllTests.class,
  org.tptp.atp.Prover9TheoremProverTest.class,
  org.tptp.atp.Prover9OutputParserTest.class,
  org.tptp.atp.JobExecutorTest.class
})
public class AllTests {
    // why on earth I need this class, I have no idea! 
}