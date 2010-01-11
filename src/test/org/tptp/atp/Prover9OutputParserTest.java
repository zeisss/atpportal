package org.tptp.atp;

import java.io.*;
import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.tptp.model.*;
import org.tptp.atp.prover9.*;

public class Prover9OutputParserTest
{
    @Test
    public void testParseProofBlock() {
        String input = "\n" +
            "% Proof 1 at 3.47 (+ 0.13) seconds: assoc_meet.\n" +
            "% Length of proof is 49.\n" +
            "% Level of proof is 14.\n" +
            "% Maximum clause weight is 21.000.\n" +
            "% Given clauses 235.\n" +
            "\n" +
            "1 (x ^ y) ^ z = x ^ (y ^ z) # label(assoc_meet) # label(non_clause) # label(goal).  [goal].\n" +
            "2 x v (y ^ (x ^ z)) = x # label(McKenzie_1).  [assumption].\n" +
            "3 x ^ (y v (x v z)) = x # label(McKenzie_2).  [assumption].\n" +
            "4 ((x ^ y) v (y ^ z)) v y = y # label(McKenzie_3).  [assumption].\n" +
            "5 ((x v y) ^ (y v z)) ^ y = y # label(McKenzie_4).  [assumption].\n" +
            "6 (c1 ^ c2) ^ c3 != c1 ^ (c2 ^ c3) # label(assoc_meet) # answer(assoc_meet).  [deny(1)].\n" +
            "7 x v (y ^ x) = x.  [para(3(a,1),2(a,1,2,2))].\n" +
            "8 x ^ (y v x) = x.  [para(2(a,1),3(a,1,2,2))].\n" +
            "9 (x ^ y) v y = y.  [para(2(a,1),4(a,1,1))].\n" +
            "11 x ^ (x v y) = x.  [para(4(a,1),3(a,1,2))].\n" +
            "15 x v (x ^ y) = x.  [para(5(a,1),2(a,1,2))].\n" +
            "16 (x ^ ((y ^ (x ^ z)) v u)) ^ (y ^ (x ^ z)) = y ^ (x ^ z).  [para(2(a,1),5(a,1,1,1))].\n" +
            "18 (x v y) ^ y = y.  [para(3(a,1),5(a,1,1))].\n" +
            "23 (x ^ ((y ^ x) v z)) ^ (y ^ x) = y ^ x.  [para(7(a,1),5(a,1,1,1))].\n" +
            "31 x v (y v x) = y v x.  [para(8(a,1),9(a,1,1))].\n" +
            "43 (x v y) v x = x v y.  [para(11(a,1),7(a,1,2))].\n" +
            "48 (x ^ y) ^ x = x ^ y.  [para(15(a,1),8(a,1,2))].\n" +
            "51 x ^ (y ^ (x ^ z)) = y ^ (x ^ z).  [para(2(a,1),18(a,1,1))].\n" +
            "53 x ^ (y ^ x) = y ^ x.  [para(7(a,1),18(a,1,1))].\n" +
            "60 (x ^ (y ^ z)) v y = y.  [para(2(a,1),31(a,1,2)),rewrite([2(6)])].\n" +
            "63 (x ^ y) v x = x.  [para(15(a,1),31(a,1,2)),rewrite([15(4)])].\n" +
            "73 (x v y) ^ x = x.  [para(43(a,1),18(a,1,1))].\n" +
            "106 x v (y ^ (z ^ x)) = x.  [para(53(a,1),2(a,1,2,2))].\n" +
            "118 (x ^ y) ^ (y ^ (x ^ z)) = y ^ (x ^ z).  [para(63(a,1),16(a,1,1,2))].\n" +
            "130 ((x ^ y) ^ z) v x = x.  [para(48(a,1),60(a,1,1))].\n" +
            "182 x ^ (y ^ (z ^ x)) = y ^ (z ^ x).  [para(106(a,1),18(a,1,1))].\n" +
            "200 (x ^ y) v (z v x) = z v x.  [para(18(a,1),130(a,1,1,1))].\n" +
            "203 (x ^ y) v (x v z) = x v z.  [para(73(a,1),130(a,1,1,1))].\n" +
            "338 (x ^ y) ^ (y ^ x) = y ^ x.  [para(63(a,1),23(a,1,1,2))].\n" +
            "346 (x ^ y) ^ ((y ^ z) ^ x) = (y ^ z) ^ x.  [para(130(a,1),23(a,1,1,2))].\n" +
            "781 (x ^ (y v z)) ^ (z ^ x) = z ^ x.  [para(200(a,1),23(a,1,1,2))].\n" +
            "797 (x ^ (y v z)) ^ (y ^ x) = y ^ x.  [para(203(a,1),23(a,1,1,2))].\n" +
            "1243 (x ^ y) v (y ^ x) = x ^ y.  [para(338(a,1),15(a,1,2))].\n" +
            "1244 x ^ y = y ^ x.  [para(338(a,1),63(a,1,1)),rewrite([1243(3)])].\n" +
            "1453 c3 ^ (c1 ^ c2) != c1 ^ (c2 ^ c3) # answer(assoc_meet).  [back_rewrite(6),rewrite([1244(5)])].\n" +
            "10367 (x ^ y) ^ (x ^ (y ^ z)) = x ^ (y ^ z).  [para(1244(a,1),118(a,1,1))].\n" +
            "10402 x ^ (y ^ (z v x)) = x ^ y.  [para(781(a,1),118(a,1,2)),rewrite([1244(5),10367(5),781(7)])].\n" +
            "10403 ((x ^ (y v z)) ^ u) ^ (u ^ (y ^ x)) = u ^ (y ^ x).  [para(797(a,1),118(a,1,2,2)),rewrite([797(10)])].\n" +
            "10404 x ^ (y ^ (x v z)) = x ^ y.  [para(797(a,1),118(a,1,2)),rewrite([1244(5),10367(5),797(7)])].\n" +
            "10435 (x ^ y) ^ (z ^ x) = (x ^ y) ^ z.  [para(15(a,1),10402(a,1,2,2))].\n" +
            "10440 x ^ ((y v x) ^ z) = x ^ z.  [para(1244(a,1),10402(a,1,2))].\n" +
            "10597 (x ^ y) ^ (y ^ z) = (y ^ z) ^ x.  [back_rewrite(346),rewrite([10435(4)])].\n" +
            "10599 x ^ ((x v y) ^ z) = x ^ z.  [para(1244(a,1),10404(a,1,2))].\n" +
            "10616 (x ^ y) ^ (y ^ z) = (x ^ y) ^ z.  [para(7(a,1),10440(a,1,2,1))].\n" +
            "10617 (x ^ y) ^ (x ^ z) = (x ^ y) ^ z.  [para(15(a,1),10440(a,1,2,1))].\n" +
            "10664 (x ^ y) ^ z = y ^ (x ^ z).  [para(10440(a,1),118(a,1,2,2)),rewrite([10616(4),10617(3),10440(5)])].\n" +
            "10667 x ^ (y ^ z) = z ^ (y ^ x).  [back_rewrite(10597),rewrite([10664(3),51(3),10664(4)])].\n" +
            "10668 x ^ (y ^ z) = z ^ (x ^ y).  [back_rewrite(10403),rewrite([10664(3),10667(6),182(5),10664(5),10599(4),51(3)])].\n" +
            "10669 $F # answer(assoc_meet).  [resolve(10668,a,1453,a(flip))].\n" +
            "\n";
        Set<Formula> used = new HashSet<Formula>();
        Set<Formula> proved = new HashSet<Formula>();
        Set<ProofStep> steps = new HashSet<ProofStep>();
        
        Prover9OutputParser parser = new Prover9OutputParser();
        parser.parseProof(input, used, proved, steps);
        
        assertEquals(49, steps.size());
        assertTrue(steps.contains(new ProofStep(10617, "(x ^ y) ^ (x ^ z) = (x ^ y) ^ z", "[para(15(a,1),10440(a,1,2,1))]")));
        
        // A step aint a proved formula
        Formula f = new Formula("", "(x ^ y) ^ (x ^ z) = (x ^ y) ^ z");
        assertFalse(proved.contains(f));        
    }
    
    @Test
    public void testParseStatistics() {
        String input = "\n" +
            "Given=235. Generated=67528. Kept=10667. proofs=1.\n" +
            "Usable=116. Sos=5331. Demods=5131. Limbo=52, Disabled=5172. Hints=0.\n" +
            "Kept_by_rule=0, Deleted_by_rule=0.\n" +
            "Forward_subsumed=56861. Back_subsumed=69.\n" +
            "Sos_limit_deleted=0. Sos_displaced=0. Sos_removed=0.\n" +
            "New_demodulators=10202 (9 lex), Back_demodulated=5098. Back_unit_deleted=0.\n" +
            "Demod_attempts=934815. Demod_rewrites=145926.\n" +
            "Res_instance_prunes=0. Para_instance_prunes=0. Basic_paramod_prunes=0.\n" +
            "Nonunit_fsub_feature_tests=0. Nonunit_bsub_feature_tests=0.\n" +
            "Megabytes=11.58.\n" +
            "User_CPU=3.47, System_CPU=0.13, Wall_clock=3.\n" +
            "\n";
        
        Map<String,String> details = new HashMap<String,String>();
        
        Prover9OutputParser parser = new Prover9OutputParser();
        parser.parseStatistics(input, details);
        
        assertEquals("235", details.get("Given"));
        assertEquals("67528", details.get("Generated"));
        assertEquals("0", details.get("Kept_by_rule"));
        assertEquals("10202 (9 lex)", details.get("New_demodulators"));
        assertEquals("5098", details.get("Back_demodulated"));
        assertEquals("3", details.get("Wall_clock"));
        assertEquals("11.58", details.get("Megabytes"));
    }
    
    
    
}