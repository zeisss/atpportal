package atpportal.tags;

import java.io.*;
import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class DumpTagTest {
    
    @Test
    public void testEscapeJavaScript() {
        assertEquals("\"\"", DumpTag.escapeJS(""));
        assertEquals("\"Hello World!\"", DumpTag.escapeJS("Hello World!"));
        assertEquals("\"Hi you & me, there\"", DumpTag.escapeJS("Hi you & me, there"));
        assertEquals("\"$!\\\"@\"", DumpTag.escapeJS("$!\"@"));
        
    }
    
    @Test
    public void testEscapeHtml() {
        assertEquals("Hello World!", DumpTag.escapeHTML("Hello World!"));
        assertEquals("Hello World!", DumpTag.escapeHTML("<b>Hello World!</b>"));
        assertEquals("Hello World!alert(&quot;Evil knevel!&quot;);", DumpTag.escapeHTML("Hello World!<script>alert(\"Evil knevel!\");</script>"));
        assertEquals("a &lt; s(0)", DumpTag.escapeHTML("a < s(0)"));
        assertEquals("a &lt;s(0)", DumpTag.escapeHTML("a <s(0)"));
        assertEquals("a &lt; s(0) &gt; c", DumpTag.escapeHTML("a < s(0) > c"));
        assertEquals("a ", DumpTag.escapeHTML("a <s(0) >")); // Yes, this kinda looks like a tag, so its cut off
        assertEquals("f &amp; b", DumpTag.escapeHTML("f & b"));
    }
}
