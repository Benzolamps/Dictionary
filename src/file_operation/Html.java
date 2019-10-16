package file_operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理HTML字符串
 *
 * @author Benzolamps
 */
public class Html {
    // 定义HTML标签的正则表达式
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?</script>";
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?</style>";
    private static final String REGEX_HTML = "<[^>]+>";
    private String htmlFormat; // HTML字符串

    public Html(String htmlFormat) {
        this.htmlFormat = htmlFormat;
    }

    public String getHtmlFormat() {
        return htmlFormat;
    }

    public void setHtmlFormat(String htmlFormat) {
        this.htmlFormat = htmlFormat;
    }

    /**
     * @return 返回htmlFormat去除HTML标签后的字符串
     */
    public String filt() {
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlFormat);
        htmlFormat = m_script.replaceAll("");

        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlFormat);
        htmlFormat = m_style.replaceAll("");

        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlFormat);
        htmlFormat = m_html.replaceAll("");

        htmlFormat = htmlFormat.replaceAll("&nbsp;", "");

        return htmlFormat.trim(); // 去掉所有空格字符
    }
}
