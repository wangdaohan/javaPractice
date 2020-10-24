package patrick;

public class CountWords {
    /**
     * Given a string, count number of words in it.
     * The words are separated by following characters: space (‘ ‘) or new line (‘\n’) or tab (‘\t’) or a combination of these.
     */
    // Driver program to test above functions
    public static void main(String args[]) {
        String str = "  One two       three\n four\tfive  ";
        System.out.println("No of words : " + countWords(str));
    }
    static final int OUT = 0;
    static final int IN = 1;
    /**
     * Time complexity: O(n)
     * 统计一段话中有多少个词，话中有包含空格、换行符(\n)、制表符(\t)
     * 思路：1.使用3个关键指针： int OUT =0; IN = 1;
     *                        state(OUT/IN)=OUT OUT代表当前字符是特殊字符中的一个 / IN代表词已统计过一次（wc+1)，不用再统计
     *                        wc = 0 :word count
     *                        i =0; 将str迭代的开始
     *
     *      2. while(i<str.length()){
     *            如果 str.charAt(i) == ' '空格 or '\n' '\t'
     *                 state = OUT
     *            否则 如果 state == OUT:
     *                 state = IN
     *                 ++wc;
     *            ++i;
     *      }
     *      return wc;
     *
     */
    // returns number of words in str
    static int countWords(String str) {
        int state = OUT;
        int wc = 0;  // word count
        int i = 0;

        // Scan all characters one by one
        while (i < str.length()) {
            // If next character is a separator, set the
            // state as OUT
            char test = str.charAt(i);
            if (str.charAt(i) == ' ' || str.charAt(i) == '\n' || str.charAt(i) == '\t') {
                state = OUT;
                // If next character is not a word separator
                // and state is OUT, then set the state as IN
                // and increment word count
            }else if (state == OUT) {
                state = IN;
                ++wc;
            }
            // Move to next character
            ++i;
        }
        return wc;
    }

}
