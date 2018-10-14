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

import java.util.Random;
import static org.addario.AnsiColors.*;

class Utils {
    static String RandomChars(int num) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomChars = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < num; i++)
            randomChars.append(chars.charAt(random.nextInt(chars.length())));

        return randomChars.toString();
    }

    static void ErrorPrintLn(String text) {
        System.out.println(RED_BACKGROUND + BLACK_BOLD + text + RESET);
    }

    static void PrintLn(String text) {
        System.out.println(AllRequests.colors.get(AllRequests.index % AllRequests.colors.size()) + text + RESET);
        AllRequests.index++;
    }
}
