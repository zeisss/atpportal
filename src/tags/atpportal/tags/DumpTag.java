package atpportal.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.*;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * The DumpTag is a utility class for writing text into the output stream.
 * It supports two modes:
 * <ul>
 *  <li>The <code>js</code> (Javascript) mode writes the value but escapes it like a javascript tag.
 *  <li>The <code>html</code> mode writes the text as is, but removes all html tags, so it cannot be used for XSS.
 * </ul>
 */
public class DumpTag extends SimpleTagSupport {
    public static String HTML_PATTERN = "\\<.*?\\>";
    private String value, mode;
    
    public void setValue(String s) {
        value = s;
    }
    
    public void setMode(String s) {
        mode = s;
    }
    
    public void doTag() throws JspException, IOException
    {
        if ( "js".equals(mode)) {
            getJspContext().getOut().print(escapeJS(this.value));
        } else if ( "html".equals(mode)) {
            getJspContext().getOut().print(escapeHTML(this.value));
        } else {
            getJspContext().getOut().print(this.value);
        }
    }
    
    public static String escapeJS(String value) {
        return "\"" + StringEscapeUtils.escapeJavaScript(value) + "\"";
    }
    
    public static String escapeHTML(String value) {
        return StringEscapeUtils.escapeHtml(value.replaceAll(HTML_PATTERN, ""));
    }
}