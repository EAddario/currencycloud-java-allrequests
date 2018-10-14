/*
 * MIT License
 *
 * Copyright (c) 2018. Ed Addario
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.addario;

public class AnsiColors {
    // Reset
    public static final String RESET = "\033[0m";

    // Regular Colors
    static final String BLACK = "\033[0;30m";
    static final String BLUE = "\033[0;34m";
    static final String CYAN = "\033[0;36m";
    static final String GREEN = "\033[0;32m";
    static final String PURPLE = "\033[0;35m";
    static final String RED = "\033[0;31m";
    static final String WHITE = "\033[0;37m";
    static final String YELLOW = "\033[0;33m";

    // Bold
    static final String BLACK_BOLD = "\033[1;30m";
    static final String BLUE_BOLD = "\033[1;34m";
    static final String CYAN_BOLD = "\033[1;36m";
    static final String GREEN_BOLD = "\033[1;32m";
    static final String PURPLE_BOLD = "\033[1;35m";
    static final String RED_BOLD = "\033[1;31m";
    static final String WHITE_BOLD = "\033[1;37m";
    static final String YELLOW_BOLD = "\033[1;33m";

    // Underline
    static final String BLACK_UNDERLINED = "\033[4;30m";
    static final String BLUE_UNDERLINED = "\033[4;34m";
    static final String CYAN_UNDERLINED = "\033[4;36m";
    static final String GREEN_UNDERLINED = "\033[4;32m";
    static final String PURPLE_UNDERLINED = "\033[4;35m";
    static final String RED_UNDERLINED = "\033[4;31m";
    static final String WHITE_UNDERLINED = "\033[4;37m";
    static final String YELLOW_UNDERLINED = "\033[4;33m";

    // Background
    static final String BLACK_BACKGROUND = "\033[40m";
    static final String BLUE_BACKGROUND = "\033[44m";
    static final String CYAN_BACKGROUND = "\033[46m";
    static final String GREEN_BACKGROUND = "\033[42m";
    static final String PURPLE_BACKGROUND = "\033[45m";
    static final String RED_BACKGROUND = "\033[41m";
    static final String WHITE_BACKGROUND = "\033[47m";
    static final String YELLOW_BACKGROUND = "\033[43m";

    // High Intensity
    static final String BLACK_BRIGHT = "\033[0;90m";
    static final String BLUE_BRIGHT = "\033[0;94m";
    static final String CYAN_BRIGHT = "\033[0;96m";
    static final String GREEN_BRIGHT = "\033[0;92m";
    static final String PURPLE_BRIGHT = "\033[0;95m";
    static final String RED_BRIGHT = "\033[0;91m";
    static final String WHITE_BRIGHT = "\033[0;97m";
    static final String YELLOW_BRIGHT = "\033[0;93m";

    // Bold High Intensity
    static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
    static final String BLUE_BOLD_BRIGHT = "\033[1;94m";
    static final String CYAN_BOLD_BRIGHT = "\033[1;96m";
    static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
    static final String RED_BOLD_BRIGHT = "\033[1;91m";
    static final String WHITE_BOLD_BRIGHT = "\033[1;97m";
    static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";

    // High Intensity backgrounds
    static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";
    static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";
    static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";
    static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";
    static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m";
    static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";
    static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";
    static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";
}
